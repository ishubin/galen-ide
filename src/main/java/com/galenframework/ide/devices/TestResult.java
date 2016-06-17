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
package com.galenframework.ide.devices;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.validation.ValidationResult;
import org.openqa.selenium.Dimension;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;

public class TestResult {
    public static final String CRASHED = "crashed";
    public static final String FAILED = "failed";
    public static final String RUNNING = "running";
    public static final String WARNING = "warning";
    public static final String PASSED = "passed";
    private final List<String> errorMessages;
    private final String name;

    private int errors;
    private int warnings;

    @JsonIgnore
    private LayoutReport layoutReport;

    private String status = RUNNING;
    private TestResultException exception;
    private String externalReport;
    private Date startedAt;
    private Date endedAt;
    private Dimension size;

    public TestResult(String name) {
        this.name = name;
        status = PASSED;
        errorMessages = Collections.emptyList();
    }

    public TestResult(LayoutReport layoutReport) {
        this.layoutReport = layoutReport;
        this.name = "Layout report: " + layoutReport.getTitle();

        errors = layoutReport.errors();
        warnings = layoutReport.warnings();

        if (errors > 0) {
            this.status = FAILED;
        } else if (warnings > 0) {
            this.status = WARNING;
        } else {
            this.status = PASSED;
        }
        this.errorMessages = collectValidationErrors(layoutReport);
    }

    public TestResult(Exception ex) {
        setStatus(CRASHED);
        this.name = ex.getClass().getName() + ": " + ex.getMessage();
        exception = new TestResultException(ex);
        this.errorMessages = Collections.emptyList();
    }

    private List<String> collectValidationErrors(LayoutReport layoutReport) {
        return collectValidationErrors(layoutReport.getValidationErrorResults(), null);
    }

    private List<String> collectValidationErrors(List<ValidationResult> validationErrorResults, String prefix) {
        List<String> errors = new LinkedList<>();

        if (validationErrorResults != null) {
            validationErrorResults.forEach( errorResult -> {
                if (errorResult.getChildValidationResults() != null && ! errorResult.getChildValidationResults().isEmpty()) {
                    errors.addAll(collectValidationErrors(errorResult.getChildValidationResults(),
                            format("Inside %s: ", findFirstObjectName(errorResult))));
                } else {
                    errorResult.getError().getMessages().forEach(message -> {
                        if (prefix != null) {
                            errors.add(prefix + message);
                        } else {
                            errors.add(message);
                        }
                    });
                }
            });
        }

        return errors;
    }

    private String findFirstObjectName(ValidationResult errorResult) {
        if (errorResult.getValidationObjects() != null && !errorResult.getChildValidationResults().isEmpty()) {
            return errorResult.getValidationObjects().get(0).getName();
        } else {
            return "unknown element";
        }
    }

    public LayoutReport getLayoutReport() {
        return layoutReport;
    }

    public void setLayoutReport(LayoutReport layoutReport) {
        this.layoutReport = layoutReport;
    }

    public TestResultException getException() {
        return exception;
    }

    public void setException(TestResultException exception) {
        this.exception = exception;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public int getWarnings() {
        return warnings;
    }

    public void setWarnings(int warnings) {
        this.warnings = warnings;
    }

    public void setExternalReport(String externalReport) {
        this.externalReport = externalReport;
    }

    public String getExternalReport() {
        return externalReport;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setEndedAt(Date endedAt) {
        this.endedAt = endedAt;
    }

    public Date getEndedAt() {
        return endedAt;
    }

    public long getDuration() {
        if (startedAt != null && endedAt != null) {
            return endedAt.getTime() - startedAt.getTime();
        }
        return 0;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    public Dimension getSize() {
        return size;
    }
}
