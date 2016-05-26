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
package com.galenframework.ide.controllers;

import com.galenframework.ide.devices.TestResult;
import com.galenframework.ide.services.results.TestResultService;
import static com.galenframework.ide.util.JsonTransformer.toJson;
import static spark.Spark.*;

public class TestResultController {
    private static final String APPLICATION_JSON = "application/json";
    private final TestResultService testResultService;

    public TestResultController(TestResultService testResultService) {
        this.testResultService = testResultService;
        initRoutes();
    }

    private void initRoutes() {
        get("/api/results", (request, response) -> {
            response.header("Content-Type", APPLICATION_JSON);
            return testResultService.getTestResultsOverview();
        }, toJson());

        get("/api/results/:reportId", (req, res) -> {
            String reportId = req.params("reportId");
            res.header("Content-Type", APPLICATION_JSON);

            TestResult testResult = testResultService.getTestResult(reportId);
            if (testResult != null) {
                return testResult;
            } else {
                throw new RuntimeException("Report doesn't exist: " + reportId);
            }
        }, toJson());

        get("/api/results/:reportId/layoutReport", (req, res) -> {
            String reportId = req.params("reportId");
            res.header("Content-Type", APPLICATION_JSON);

            TestResult testResult = testResultService.getTestResult(reportId);
            if (testResult != null) {
                return testResult.getLayoutReport();
            } else {
                throw new RuntimeException("Report doesn't exist: " + reportId);
            }
        }, toJson());
        delete("/api/results", (req, res) -> {
            testResultService.clearAllTestResults();
            return "cleared";
        });
    }
}
