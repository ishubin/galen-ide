package com.galenframework.quicktester;

import com.fasterxml.jackson.databind.ObjectMapper;

import static com.galenframework.quicktester.JsonTransformer.toJson;
import static spark.Spark.*;


public class TesterController {
    private static final String APPLICATION_JSON = "application/json";
    private TesterService testerService = new TesterService();
    ObjectMapper mapper = new ObjectMapper();


    public TesterController() {
        post("/api/tester/test", (request, response) -> {
            TestCommand testCommand = mapper.readValue(request.body(), TestCommand.class);
            testerService.syncAllBrowsers();
            testerService.testAllBrowsers(testCommand.getSpec());
            return "Started testing: " + testCommand.getSpec();
        });

        get("/api/tester/results", (request, response) -> {
            response.header("Content-Type", APPLICATION_JSON);
            return testerService.getTestResults();
        }, toJson());
    }
}
