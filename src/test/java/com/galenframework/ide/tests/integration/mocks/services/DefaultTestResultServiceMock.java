package com.galenframework.ide.tests.integration.mocks.services;

import com.galenframework.ide.TestResultContainer;
import com.galenframework.ide.TestResultsOverview;
import com.galenframework.ide.devices.TestResult;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.results.TestResultService;
import org.openqa.selenium.Dimension;

import java.util.Collections;
import java.util.List;

public class DefaultTestResultServiceMock implements TestResultService {

    @Override
    public TestResultsOverview getTestResultsOverview(RequestData requestData) {
        return new TestResultsOverview(Collections.<TestResultContainer>emptyList(), null);
    }

    @Override
    public void clearAllTestResults(RequestData requestData) {
    }

    @Override
    public String registerNewTestResultContainer(RequestData requestData, String deviceName, List<String> tags, Dimension size) {
        return "some-new-test-result-container";
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return null;
    }

    @Override
    public void onTestResult(String testUniqueId, TestResult testResult) {

    }
}
