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
package com.galenframework.ide.model.results;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class CommandExecutionResult {
    private ExecutionStatus status = ExecutionStatus.planned;
    private String externalReport;

    @JsonIgnore
    private Object data;
    private String errorMessage;
    private String externalReportFolder;

    public CommandExecutionResult() {
    }

    public CommandExecutionResult(ExecutionStatus status) {
        this.status = status;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public static CommandExecutionResult passed() {
        return new CommandExecutionResult(ExecutionStatus.passed);
    }

    public String getExternalReport() {
        return externalReport;
    }

    public void setExternalReport(String externalReport) {
        this.externalReport = externalReport;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public static CommandExecutionResult error(Throwable ex) {
        CommandExecutionResult result = new CommandExecutionResult();
        result.setStatus(ExecutionStatus.failed);
        result.setErrorMessage(ExceptionUtils.getMessage(ex) + "\n" + ExceptionUtils.getStackTrace(ex));
        return result;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static CommandExecutionResult skipped() {
        return new CommandExecutionResult(ExecutionStatus.skipped);
    }

    public void setExternalReportFolder(String externalReportFolder) {
        this.externalReportFolder = externalReportFolder;
    }

    public String getExternalReportFolder() {
        return externalReportFolder;
    }
}
