package com.galenframework.quicktester.devices.commands;

import com.galenframework.api.Galen;
import com.galenframework.quicktester.devices.Device;
import com.galenframework.quicktester.devices.DeviceCommand;
import com.galenframework.quicktester.devices.TestResult;
import com.galenframework.quicktester.devices.TestResultsListener;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import com.galenframework.reports.model.LayoutReport;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

public class DeviceCheckLayoutCommand extends DeviceCommand {
    private final String spec;
    private final TestResultsListener testResultsListener;
    private final String uniqueId;

    public DeviceCheckLayoutCommand(String uniqueId, String spec, TestResultsListener testResultsListener) {
        this.uniqueId = uniqueId;
        this.spec = spec;
        this.testResultsListener = testResultsListener;
    }

    @Override
    public void execute(Device device, WebDriver driver) {
        TestResult testResult;
        try {
            LayoutReport layoutReport = Galen.checkLayout(driver, "specs/" + spec, device.getTags());
            testResult = new TestResult(layoutReport);

            HtmlReportBuilder reportBuilder = new HtmlReportBuilder();
            String tempDirectoryPath = Files.createTempDirectory("galen-html-report").toString();
            reportBuilder.build(createTestInfo(spec, layoutReport), tempDirectoryPath);

            testResult.setExternalReport(tempDirectoryPath + "/report.html");

        } catch (Exception ex) {
            ex.printStackTrace();
            testResult = new TestResult(ex);
        }
        testResultsListener.onTestResult(uniqueId, testResult);
    }

    private List<GalenTestInfo> createTestInfo(String spec, LayoutReport layoutReport) {
        List<GalenTestInfo> testInfos = new LinkedList<>();
        GalenTestInfo testInfo = new GalenTestInfo(spec, null);
        testInfo.getReport().layout(layoutReport, spec);
        testInfos.add(testInfo);
        return testInfos;
    }
}
