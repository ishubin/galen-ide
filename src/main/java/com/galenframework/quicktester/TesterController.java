package com.galenframework.quicktester;

import com.fasterxml.jackson.databind.ObjectMapper;

import static com.galenframework.quicktester.JsonTransformer.toJson;
import static spark.Spark.*;


public class TesterController {
    private static final String APPLICATION_JSON = "application/json";
    private final String reportStoragePath;
    private TesterService testerService = new TesterService();
    private SpecsBrowserService specsBrowserService = new SpecsBrowserService();
    ObjectMapper mapper = new ObjectMapper();


    public TesterController(String reportStoragePath) {
        this.reportStoragePath = reportStoragePath;

        initRoutes();
    }

    private void initRoutes() {
        post("/api/tester/test", (request, response) -> {
            TestCommand testCommand = mapper.readValue(request.body(), TestCommand.class);
            testerService.syncAllBrowsers();
            testerService.testAllBrowsers(testCommand.getSpec(), reportStoragePath);
            return "Started testing: " + testCommand.getSpec();
        });

        get("/api/tester/results", (request, response) -> {
            response.header("Content-Type", APPLICATION_JSON);
            return testerService.getTestResults();
        }, toJson());

        get("/api/specs", (req, res) -> {
            return specsBrowserService.getFiles();
        }, toJson());


        get("api/devices", (request, response) -> testerService.getAllDevices(), toJson());
    }

}
