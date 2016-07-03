package com.galenframework.ide.model.results;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class CommandExecutionResult {
    private ExecutionStatus status = ExecutionStatus.planned;
    private String externalReport;

    @JsonIgnore
    private Object data;
    private String errorMessage;

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
}
