package com.galenframework.ide.services.results;

import com.galenframework.ide.TestResultContainer;
import com.galenframework.ide.TestResultsOverview;
import com.galenframework.ide.devices.TestResult;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.ServiceProvider;
import org.openqa.selenium.Dimension;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class TestResultServiceImpl implements TestResultService{

    private final ServiceProvider serviceProvider;
    private final List<TestResultContainer> testResults = Collections.synchronizedList(new LinkedList<>());

    public TestResultServiceImpl(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    @Override
    public TestResultsOverview getTestResultsOverview(RequestData requestData) {
        return new TestResultsOverview(testResults, serviceProvider.testerService().getLastTestCommand(requestData));
    }

    @Override
    public void onTestResult(String testUniqueId, TestResult testResult) {
        Optional<TestResultContainer> foundTestResult = testResults.stream().filter((r) -> r.getUniqueId().equals(testUniqueId)).findFirst();

        if (foundTestResult.isPresent()) {
            foundTestResult.get().setTestResult(testResult);
        }
    }

    @Override
    public void clearAllTestResults(RequestData requestData) {
        this.testResults.clear();
    }

    @Override
    public String registerNewTestResultContainer(RequestData requestData, String deviceName, List<String> tags, Dimension size) {
        TestResultContainer testResultContainer = new TestResultContainer(deviceName, tags, size);
        testResults.add(testResultContainer);
        return testResultContainer.getUniqueId();
    }

}
