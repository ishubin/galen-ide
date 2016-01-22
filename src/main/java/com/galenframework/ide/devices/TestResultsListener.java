package com.galenframework.ide.devices;

public interface TestResultsListener {
    void onTestResult(String testUniqueId, TestResult testResult);
}
