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
package com.galenframework.ide.devices.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.galenframework.api.Galen;
import com.galenframework.ide.Settings;
import com.galenframework.ide.devices.*;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.SectionFilter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openqa.selenium.Dimension;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;

public class DeviceCheckLayoutCommand extends DeviceCommand {
    public static final String CHECK_LAYOUT = "checkLayout";
    public static final String REPORT_HTML = "report.html";

    private final String spec;
    private final String reportId;
    private final String reportStoragePath;
    private final List<String> tags;

    @JsonIgnore
    private final TestResultsListener testResultsListener;

    @JsonIgnore
    private final Settings settings;

    @JsonIgnore
    private final static File onePixelImage = createOnePixelFakeImage();


    public DeviceCheckLayoutCommand(Settings settings, String reportId, String spec, List<String> tags, TestResultsListener testResultsListener, String reportStoragePath) {
        this.settings = settings;
        this.reportId = reportId;
        this.spec = spec;
        this.tags = tags;
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
                        device.getDriver(), spec, tags);
            } else {
                layoutReport = Galen.checkLayout(
                        device.getDriver(), spec,
                        new SectionFilter(tags, null), null, null, onePixelImage);
            }

            testResult = new TestResult(layoutReport);

            HtmlReportBuilder reportBuilder = new HtmlReportBuilder();
            String reportDir = reportId + "-" + new Date().getTime();
            String reportDirPath = reportStoragePath + File.separator + reportDir;

            Dimension size = device.getDriver().manage().window().getSize();
            reportBuilder.build(createTestInfo(device, spec, size, layoutReport), reportDirPath);

            testResult.setExternalReport(reportDir + "/" + findTestHtmlFileIn(reportDirPath));

            testResult.setStartedAt(startedAt);
            testResult.setEndedAt(new Date());
            testResult.setDuration(testResult.getEndedAt().getTime() - testResult.getStartedAt().getTime());

        } catch (Exception ex) {
            ex.printStackTrace();
            testResult = new TestResult(ex);
        }

        testResultsListener.onTestResult(reportId, testResult);
    }

    @Override
    public String getName() {
        return CHECK_LAYOUT;
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

    private List<GalenTestInfo> createTestInfo(Device device, String spec, Dimension size, LayoutReport layoutReport) {
        List<GalenTestInfo> testInfos = new LinkedList<>();
        GalenTestInfo testInfo = new GalenTestInfo(format("%s on %s with size %dx%d", spec, device.getName(), size.width, size.height), null);
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

    public String getSpec() {
        return spec;
    }

    public String getReportId() {
        return reportId;
    }

    public String getReportStoragePath() {
        return reportStoragePath;
    }

    public List<String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("spec", spec)
                .append("tags", tags)
                .append("reportId", reportId)
                .toString();
    }
}
