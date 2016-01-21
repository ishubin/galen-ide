package com.galenframework.quicktester.devices.commands;

import com.galenframework.api.Galen;
import com.galenframework.quicktester.Settings;
import com.galenframework.quicktester.devices.*;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.SectionFilter;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.Dimension;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;

public class DeviceCheckLayoutCommand extends DeviceCommand {
    public static final String REPORT_HTML = "report.html";
    private final String spec;
    private final TestResultsListener testResultsListener;
    private final String uniqueId;
    private final String reportStoragePath;
    private final Dimension size;
    private final Settings settings;
    private final static File onePixelImage = createOnePixelFakeImage();


    public DeviceCheckLayoutCommand(Settings settings, String uniqueId, Dimension size, String spec, TestResultsListener testResultsListener, String reportStoragePath) {
        this.settings = settings;
        this.uniqueId = uniqueId;
        this.size = size;
        this.spec = spec;
        this.testResultsListener = testResultsListener;
        this.reportStoragePath = reportStoragePath;
    }

    @Override
    public void execute(Device device, DeviceThread deviceThread) {
        TestResult testResult;
        try {
            Date startedAt = new Date();
            LayoutReport layoutReport;

            if (settings.isMakeScreenshots()) {
                layoutReport = Galen.checkLayout(
                        device.getDriver(), spec, device.getTags());
            } else {
                layoutReport = Galen.checkLayout(
                        device.getDriver(), spec,
                        new SectionFilter(device.getTags(), null), null, null, onePixelImage);
            }

            testResult = new TestResult(layoutReport);

            HtmlReportBuilder reportBuilder = new HtmlReportBuilder();
            String reportDir = uniqueId + "-" + new Date().getTime();
            String reportDirPath = reportStoragePath + File.separator + reportDir;
            reportBuilder.build(createTestInfo(spec, size, layoutReport), reportDirPath);

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

    private List<GalenTestInfo> createTestInfo(String spec, Dimension size, LayoutReport layoutReport) {
        List<GalenTestInfo> testInfos = new LinkedList<>();
        GalenTestInfo testInfo = new GalenTestInfo(spec + format(", size %dx%d", size.width, size.height), null);
        testInfo.getReport().layout(layoutReport, spec);
        testInfos.add(testInfo);
        return testInfos;
    }

    private static File createOnePixelFakeImage() {
        try {
            File file = File.createTempFile("1-pixel-image", ".png");
            FileWriter fw = new FileWriter(file);
            IOUtils.copy(DeviceCheckLayoutCommand.class.getResourceAsStream("/public/images/1-pixel-image.png"), fw);
            fw.flush();
            fw.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
