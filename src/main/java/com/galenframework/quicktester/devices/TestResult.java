package com.galenframework.quicktester.devices;

import com.galenframework.reports.model.LayoutReport;

public class TestResult {
    public static final String FAILED = "failed";
    public static final String RUNNING = "running";
    private static final String WARNING = "warning";
    public static final String PASSED = "passed";
    private LayoutReport layoutReport;
    private boolean hasCrashed = false;
    private String status = RUNNING;
    private TestResultException exception;

    public TestResult(LayoutReport layoutReport) {
        this.layoutReport = layoutReport;

        if (layoutReport.errors() > 0) {
            this.status = FAILED;
        } else if (layoutReport.warnings() > 0) {
            this.status = WARNING;
        } else {
            this.status = PASSED;
        }

        if (layoutReport.getValidationErrorResults() != null && !layoutReport.getValidationErrorResults().isEmpty()) {
        }

    }

    public TestResult(Exception ex) {
        setHasCrashed(true);
        exception = new TestResultException(ex);
    }

    public LayoutReport getLayoutReport() {
        return layoutReport;
    }

    public void setLayoutReport(LayoutReport layoutReport) {
        this.layoutReport = layoutReport;
    }


    public boolean isHasCrashed() {
        return hasCrashed;
    }

    public void setHasCrashed(boolean hasCrashed) {
        this.hasCrashed = hasCrashed;
    }

    public TestResultException getException() {
        return exception;
    }

    public void setException(TestResultException exception) {
        this.exception = exception;
    }
}
