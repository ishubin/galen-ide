package com.galenframework.quicktester.devices;

import java.util.List;

public interface TestResultsListener {
    void onTestResults(List<TestResult> testResults);
}
