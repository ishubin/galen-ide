package com.galenframework.quicktester.devices;

public interface TestResultsListener {
    void onTestResult(String testUniqueId, TestResult testResult);
}
