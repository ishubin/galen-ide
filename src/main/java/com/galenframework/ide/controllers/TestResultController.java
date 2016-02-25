package com.galenframework.ide.controllers;

import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.results.TestResultService;
import static com.galenframework.ide.JsonTransformer.toJson;
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
            return testResultService.getTestResultsOverview(new RequestData(request));
        }, toJson());
    }
}
