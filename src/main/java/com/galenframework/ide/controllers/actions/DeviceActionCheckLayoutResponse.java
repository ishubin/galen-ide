package com.galenframework.ide.controllers.actions;

public class DeviceActionCheckLayoutResponse {
    private final String reportId;

    public DeviceActionCheckLayoutResponse(String reportId) {
        this.reportId = reportId;
    }

    public String getReportId() {
        return reportId;
    }
}
