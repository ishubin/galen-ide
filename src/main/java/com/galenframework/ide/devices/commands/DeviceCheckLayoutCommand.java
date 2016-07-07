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
import com.galenframework.ide.model.results.CommandExecutionResult;
import com.galenframework.ide.model.results.ExecutionStatus;
import com.galenframework.ide.model.settings.Settings;
import com.galenframework.ide.devices.*;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.validation.ValidationError;
import com.galenframework.validation.ValidationResult;
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

    private String spec;
    private List<String> tags;

    @JsonIgnore
    private final static File onePixelImage = createOnePixelFakeImage();


    public DeviceCheckLayoutCommand() {
    }

    public DeviceCheckLayoutCommand(String spec, List<String> tags) {
        this.spec = spec;
        this.tags = tags;
    }

    @Override
    public CommandExecutionResult execute(Device device, DeviceExecutor deviceExecutor, String taskId, Settings settings, String reportStoragePath) throws Exception {
        try {
            LayoutReport layoutReport;

            Dimension size = device.getDriver().manage().window().getSize();
            if (settings.isMakeScreenshots()) {
                layoutReport = Galen.checkLayout(
                        device.getDriver(), spec, tags);
            } else {
                layoutReport = Galen.checkLayout(
                        device.getDriver(), spec,
                        new SectionFilter(tags, null), null, null, onePixelImage);
            }

            HtmlReportBuilder reportBuilder = new HtmlReportBuilder();
            String reportDir = taskId + "-" + getCommandId();

            String reportDirPath = reportStoragePath + File.separator + reportDir;

            reportBuilder.build(createTestInfo(device, spec, size, layoutReport), reportDirPath);

            CommandExecutionResult result = new CommandExecutionResult();
            result.setExternalReport(reportDir + "/" + findTestHtmlFileIn(reportDirPath));
            result.setExternalReportFolder(reportDir);
            result.setStatus(identifyStatus(layoutReport));
            result.setErrorMessages(provideErrorMessages(layoutReport));
            result.setData(layoutReport);
            return result;

        } catch (Exception ex) {
            return CommandExecutionResult.error(ex);
        }
    }

    private List<String> provideErrorMessages(LayoutReport layoutReport) {
        if (layoutReport.getValidationErrorResults() != null) {
            List<String> errorMessages = new LinkedList<>();
            for (ValidationResult validationResult : layoutReport.getValidationErrorResults()) {
                if (validationResult.getError() != null && validationResult.getError().getMessages() != null) {
                    errorMessages.addAll(validationResult.getError().getMessages());
                }
            }
            return errorMessages;
        }
        return null;
    }

    private ExecutionStatus identifyStatus(LayoutReport layoutReport) {
        if (layoutReport.errors() > 0) {
            return ExecutionStatus.failed;
        } else if (layoutReport.warnings() > 0) {
            return ExecutionStatus.warning;
        }
        return ExecutionStatus.passed;
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

    public List<String> getTags() {
        return tags;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("spec", spec)
                .append("tags", tags)
                .toString();
    }
}
