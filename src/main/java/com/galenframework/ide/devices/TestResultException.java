package com.galenframework.ide.devices;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class TestResultException {
    private String exceptionClass;
    private String exceptionMessage;
    private String stackTrace;

    public TestResultException(Exception ex) {
        this.exceptionClass = ex.getClass().getName();
        this.exceptionMessage = ex.getMessage();
        this.stackTrace = ExceptionUtils.getStackTrace(ex);
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
