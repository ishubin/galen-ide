package com.galenframework.quicktester;

import com.galenframework.quicktester.devices.Device;
import com.galenframework.quicktester.devices.TestResult;
import com.galenframework.quicktester.devices.TestResultsListener;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

public class TesterService implements TestResultsListener {
    private WebDriver masterDriver;
    private List<Device> devices = new LinkedList<>();
    private List<TestResultContainer> testResults = Collections.synchronizedList(new LinkedList<>());

    public TesterService() {
        masterDriver = createDriver();
        devices.add(new Device(createDriver(), asList("mobile"), asList(size(450, 600))));
        devices.add(new Device(createDriver(), asList("tablet"), asList(size(600, 600))));
        devices.add(new Device(createDriver(), asList("desktop"), asList(size(1024, 768))));

        devices.forEach((device -> device.start()));
        System.out.println("Started all devices");
    }

    private WebDriver createDriver() {
        WebDriver driver = new FirefoxDriver();
        driver.get("http://testapp.galenframework.com");
        return driver;
    }

    public void syncAllBrowsers() {
        String originSource = masterDriver.getPageSource();
        String url = masterDriver.getCurrentUrl();

        devices.forEach((device) -> device.injectSource(url, originSource));
    }

    public void testAllBrowsers(String spec) {
        this.testResults.clear();
        devices.forEach((device) -> {
            TestResultContainer testResultContainer = registerNewTestResultContainer(device);
            device.checkLayout(testResultContainer.getUniqueId(), spec, this);
        });
    }

    private TestResultContainer registerNewTestResultContainer(Device device) {
        TestResultContainer testResultContainer = new TestResultContainer(
                "Firefox " + device.getTags().get(0),
                device.getTags(),
                device.getSizes().get(0)
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
}
