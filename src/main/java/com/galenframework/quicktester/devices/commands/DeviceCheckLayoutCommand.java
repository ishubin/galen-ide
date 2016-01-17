package com.galenframework.quicktester.devices.commands;

import com.galenframework.api.Galen;
import com.galenframework.quicktester.devices.Device;
import com.galenframework.quicktester.devices.DeviceCommand;
import com.galenframework.quicktester.devices.TestResult;
import com.galenframework.quicktester.devices.TestResultsListener;
import org.openqa.selenium.WebDriver;

import java.util.LinkedList;
import java.util.List;

public class DeviceCheckLayoutCommand extends DeviceCommand {
    private final String spec;
    private final TestResultsListener testResultsListener;

    public DeviceCheckLayoutCommand(String spec, TestResultsListener testResultsListener) {
        this.spec = spec;
        this.testResultsListener = testResultsListener;
    }

    @Override
    public void execute(Device device, WebDriver driver) {
        List<TestResult> testResults = new LinkedList<>();
        try {
            testResults.add(new TestResult(Galen.checkLayout(driver, "specs/" + spec, device.getTags())));
        } catch (Exception ex) {
            ex.printStackTrace();
            testResults.add(new TestResult(ex));
        }
        testResultsListener.onTestResults(testResults);
    }
}
