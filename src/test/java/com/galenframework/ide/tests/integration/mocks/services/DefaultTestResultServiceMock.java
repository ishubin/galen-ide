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
