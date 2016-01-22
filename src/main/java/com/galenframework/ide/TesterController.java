package com.galenframework.ide;

import com.fasterxml.jackson.databind.ObjectMapper;

import static com.galenframework.ide.JsonTransformer.toJson;
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
            testerService.testAllBrowsers(testCommand.getSpecPath(), reportStoragePath);
            return "Started testing: " + testCommand.getSpecPath();
        });

        get("/api/tester/results", (request, response) -> {
            response.header("Content-Type", APPLICATION_JSON);
            return testerService.getTestResults();
        }, toJson());

        get("/api/specs", (req, res) -> {
            return specsBrowserService.getFiles();
        }, toJson());

        get("/api/specs-content/*", (req, res) -> {
            String[] splat = req.splat();
            if (splat.length > 0) {
                return FileContent.fromFile(splat[0]);
            } else throw new RuntimeException("Incorrect request");
        }, toJson());

        get("api/devices", (request, response) -> testerService.getAllDevices(), toJson());

        post("api/devices", (req, res) -> {
            CreateDeviceRequest createDeviceRequest = mapper.readValue(req.body(), CreateDeviceRequest.class);
            testerService.createDevice(createDeviceRequest);
            return "created";
        });

        delete("api/devices/:deviceId", (req, res) -> {
            String deviceId = req.params("deviceId");
            if (deviceId != null && !deviceId.trim().isEmpty()) {
                testerService.shutdownDevice(deviceId);
                return "Delete device " + deviceId;
            } else throw new RuntimeException("Incorrect request, missing device id");
        }, toJson());

        get("api/settings", ((request, response) -> testerService.getSettings()), toJson());

        post("api/settings", ((request, response) -> {
            Settings settings = mapper.readValue(request.body(), Settings.class);
            testerService.applySettings(settings);
            return "saved";
        }));
    }

}
