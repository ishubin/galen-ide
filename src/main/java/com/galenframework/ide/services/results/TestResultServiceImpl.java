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
package com.galenframework.ide.services.results;

import com.galenframework.ide.TestResultContainer;
import com.galenframework.ide.TestResultsOverview;
import com.galenframework.ide.devices.TestResult;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.ServiceProvider;

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
    public void onTestResult(String reportId, TestResult testResult) {
        Optional<TestResultContainer> foundTestResult = testResults.stream().filter((r) -> r.getUniqueId().equals(reportId)).findFirst();

        if (foundTestResult.isPresent()) {
            foundTestResult.get().setTestResult(testResult);
        }
    }

    @Override
    public void clearAllTestResults(RequestData requestData) {
        this.testResults.clear();
    }

    @Override
    public String registerNewTestResultContainer(RequestData requestData, String deviceName, List<String> tags) {
        TestResultContainer testResultContainer = new TestResultContainer(deviceName, tags);
        testResults.add(testResultContainer);
        return testResultContainer.getUniqueId();
    }

    @Override
    public TestResult getTestResult(RequestData requestData, String reportId) {
        Optional<TestResultContainer> testResultOption = testResults.stream().filter(trc -> trc.getUniqueId().equals(reportId)).findFirst();

        if (testResultOption.isPresent()) {
            return testResultOption.get().getTestResult();
        } else {
            return null;
        }
    }

}
