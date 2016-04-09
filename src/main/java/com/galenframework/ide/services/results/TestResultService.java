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

import com.galenframework.ide.TestResultsOverview;
import com.galenframework.ide.devices.TestResult;
import com.galenframework.ide.devices.TestResultsListener;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.Service;
import org.openqa.selenium.Dimension;

import java.util.List;

public interface TestResultService extends TestResultsListener, Service {

    TestResultsOverview getTestResultsOverview(RequestData requestData);

    void clearAllTestResults(RequestData requestData);

    String registerNewTestResultContainer(RequestData requestData, String deviceName,
                                          List<String> tags, Dimension size);

    TestResult getTestResult(RequestData requestData, String reportId);
}
