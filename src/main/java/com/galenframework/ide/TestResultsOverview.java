package com.galenframework.ide;

import java.util.List;

public class TestResultsOverview {
    private final List<TestResultContainer> testResults;
    private final TestCommand lastTestCommand;

    public TestResultsOverview(List<TestResultContainer> testResults, TestCommand lastTestCommand) {
        this.testResults = testResults;
        this.lastTestCommand = lastTestCommand;
    }

    public List<TestResultContainer> getTestResults() {
        return testResults;
    }

    public TestCommand getLastTestCommand() {
        return lastTestCommand;
    }
}
