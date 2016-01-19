package com.galenframework.quicktester;

import com.galenframework.quicktester.devices.Device;
import com.galenframework.quicktester.devices.DeviceThread;
import com.galenframework.quicktester.devices.TestResult;
import com.galenframework.quicktester.devices.TestResultsListener;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class TesterService implements TestResultsListener {
    private WebDriver masterDriver;
    private List<DeviceThread> devices = new LinkedList<>();
    private List<TestResultContainer> testResults = Collections.synchronizedList(new LinkedList<>());

    public TesterService() {
        masterDriver = new FirefoxDriver();
        masterDriver.get("http://localhost:8080");
        masterDriver.manage().window().maximize();

        devices.add(new DeviceThread(new Device("Firefox mobile", "firefox", asList("mobile"), asList(size(450, 600), size(480, 600), size(500, 600)))));
        devices.add(new DeviceThread(new Device("Firefox tablet", "firefox", asList("tablet"), asList(size(600, 600), size(700, 600), size(800, 600)))));
        devices.add(new DeviceThread(new Device("Firefox desktop", "chrome", asList("desktop"), asList(size(1024, 768), size(1100, 768), size(1200, 768)))));

        devices.forEach((device -> {
            device.start();
            device.createDriverFromClass(FirefoxDriver.class);
            device.openUrl("http://localhost:8080");
        }));
    }

    public void syncAllBrowsers() {
        String originSource = masterDriver.getPageSource();
        String url = masterDriver.getCurrentUrl();

        devices.forEach((device) -> device.injectSource(url, originSource));
    }

    public void testAllBrowsers(String spec, String reportStoragePath) {
        this.testResults.clear();
        devices.forEach((device) ->
            device.getSizes().forEach(size -> {
                TestResultContainer testResultContainer = registerNewTestResultContainer(device, size);
                device.resize(size);
                device.checkLayout(testResultContainer.getUniqueId(), size, spec, this, reportStoragePath);
            })
        );
    }

    private TestResultContainer registerNewTestResultContainer(DeviceThread deviceThread, Dimension size) {
        TestResultContainer testResultContainer = new TestResultContainer(
                deviceThread.getDevice().getName(),
                deviceThread.getTags(),
                size
        );
        testResults.add(testResultContainer);
        return testResultContainer;
    }

    @Override
    public void onTestResult(String testUniqueId, TestResult testResult) {
        Optional<TestResultContainer> foundTestResult = testResults.stream().filter((r) -> r.getUniqueId().equals(testUniqueId)).findFirst();

        if (foundTestResult.isPresent()) {
            foundTestResult.get().setTestResult(testResult);
        }
    }

    private Dimension size(int w, int h) {
        return new Dimension(w, h);
    }

    public List<TestResultContainer> getTestResults() {
        return testResults;
    }

    public List<Device> getAllDevices() {
        return devices.stream().map(d -> d.getDevice()).collect(Collectors.toList());
    }
}
