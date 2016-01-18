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

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DeviceCheckLayoutCommand extends DeviceCommand {
    public static final String REPORT_HTML = "report.html";
    private final String spec;
    private final TestResultsListener testResultsListener;
    private final String uniqueId;
    private final String reportStoragePath;

    public DeviceCheckLayoutCommand(String uniqueId, String spec, TestResultsListener testResultsListener, String reportStoragePath) {
        this.uniqueId = uniqueId;
        this.spec = spec;
        this.testResultsListener = testResultsListener;
        this.reportStoragePath = reportStoragePath;
    }

    @Override
    public void execute(Device device, WebDriver driver) {
        TestResult testResult;
        try {
            Date startedAt = new Date();
            LayoutReport layoutReport = Galen.checkLayout(driver, "specs/" + spec, device.getTags());

            testResult = new TestResult(layoutReport);

            HtmlReportBuilder reportBuilder = new HtmlReportBuilder();
            String reportDir = uniqueId + "-" + new Date().getTime();
            String reportDirPath = reportStoragePath + File.separator + reportDir;
            reportBuilder.build(createTestInfo(spec, layoutReport), reportDirPath);

            testResult.setExternalReport(reportDir + "/" + findTestHtmlFileIn(reportDirPath));

            testResult.setStartedAt(startedAt);
            testResult.setEndedAt(new Date());
            testResult.setDuration(testResult.getEndedAt().getTime() - testResult.getStartedAt().getTime());

        } catch (Exception ex) {
            ex.printStackTrace();
            testResult = new TestResult(ex);
        }

        testResultsListener.onTestResult(uniqueId, testResult);
    }

    private String findTestHtmlFileIn(String reportDirPath) {
        File dir = new File(reportDirPath);

        if (dir.exists()) {
            for (String name : dir.list()) {
                if (name.endsWith(".html") && !name.equals(REPORT_HTML)) {
                    return name;
                }
            }
        }

        return REPORT_HTML;
    }

    private List<GalenTestInfo> createTestInfo(String spec, LayoutReport layoutReport) {
        List<GalenTestInfo> testInfos = new LinkedList<>();
        GalenTestInfo testInfo = new GalenTestInfo(spec, null);
        testInfo.getReport().layout(layoutReport, spec);
        testInfos.add(testInfo);
        return testInfos;
    }
}
