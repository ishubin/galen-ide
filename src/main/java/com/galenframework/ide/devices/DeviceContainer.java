/*******************************************************************************
* Copyright 2016 Ivan Shubin http://galenframework.com
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*   http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
package com.galenframework.ide.devices;

import com.galenframework.ide.Settings;
import com.galenframework.ide.Size;
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
        masterDriver.get("http://localhost:8080");
        masterDriver.manage().window().maximize();
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

    private Size size(int w, int h) {
        return new Size(w, h);
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
