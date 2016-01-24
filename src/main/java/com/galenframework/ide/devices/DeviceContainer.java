package com.galenframework.ide.devices;

import com.galenframework.ide.Settings;
import com.galenframework.ide.TestResultContainer;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeviceContainer implements TestResultsListener {
    private WebDriver masterDriver;
    private List<DeviceThread> devices = new LinkedList<>();
    private List<TestResultContainer> testResults = Collections.synchronizedList(new LinkedList<>());
    private Settings settings = new Settings();


    public DeviceContainer() {
        masterDriver = new FirefoxDriver();
        masterDriver.get("http://testapp.galenframework.com");
        masterDriver.manage().window().maximize();

        /*devices.add(new DeviceThread(new Device("Firefox mobile", "firefox", asList("mobile"), new SizeProviderCustom(asList(size(450, 600), size(480, 600), size(500, 600))))));
        devices.add(new DeviceThread(new Device("Firefox tablet", "firefox", asList("tablet"), asList(size(600, 600), size(700, 600), size(800, 600)))));
        devices.add(new DeviceThread(new Device("Firefox desktop", "firefox", asList("desktop"), asList(size(1024, 768), size(1100, 768), size(1200, 768)))));
        devices.forEach((device -> {
            device.start();
            device.createDriverFromClass(FirefoxDriver.class);
            //device.openUrl("http://localhost:8080");
        }));
        */
    }

    public List<TestResultContainer> getTestResults() {
        return testResults;
    }

    public List<Device> getAllDevices() {
        return devices.stream().map(d -> d.getDevice()).collect(Collectors.toList());
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public WebDriver getMasterDriver() {
        return masterDriver;
    }

    public void setMasterDriver(WebDriver masterDriver) {
        this.masterDriver = masterDriver;
    }

    public List<DeviceThread> getDeviceThreads() {
        return devices;
    }

    public void clearAllTestResults() {
        this.testResults.clear();
    }

    public TestResultContainer registerNewTestResultContainer(DeviceThread deviceThread, Dimension size) {
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

    public void addDeviceThread(DeviceThread deviceThread) {
        devices.add(deviceThread);
        deviceThread.start();
    }

    public void shutdownDevice(String deviceId) {
        Optional<DeviceThread> deviceOption = devices.stream().filter(d -> d.getDevice().getDeviceId().equals(deviceId)).findFirst();
        if (deviceOption.isPresent()) {
            deviceOption.get().shutdownDevice();
        } else {
            throw new RuntimeException("Unknown device: " + deviceId);
        }
    }

    public void shutdownAllDevices() {
        devices.stream().forEach(DeviceThread::shutdownDevice);
    }
}
