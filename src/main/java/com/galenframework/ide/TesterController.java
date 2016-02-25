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
package com.galenframework.ide;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.services.filebrowser.FileBrowserService;
import com.galenframework.ide.services.filebrowser.FileContent;
import com.galenframework.ide.services.profiles.ProfilesService;
import com.galenframework.ide.services.results.TestResultService;
import com.galenframework.ide.services.settings.SettingsService;
import com.galenframework.ide.services.tester.TesterService;

import static com.galenframework.ide.JsonTransformer.toJson;
import static spark.Spark.*;


public class TesterController {
    private static final String APPLICATION_JSON = "application/json";
    private final DeviceService deviceService;
    private final TesterService testerService;
    private final FileBrowserService fileBrowserService;
    private final ProfilesService profilesService;
    private final SettingsService settingsService;
    private final TestResultService testResultService;

    ObjectMapper mapper = new ObjectMapper();

    public TesterController(DeviceService deviceService, TesterService testerService,
                            FileBrowserService fileBrowserService, ProfilesService profilesService,
                            SettingsService settingsService, TestResultService testResultService) {
        this.deviceService = deviceService;
        this.testerService  = testerService;
        this.fileBrowserService = fileBrowserService;
        this.profilesService = profilesService;
        this.settingsService = settingsService;
        this.testResultService = testResultService;
        initRoutes();
    }

    private void initRoutes() {
        get("/api/dom/:domId", (req, res) -> {
            res.header("Content-Type", "text/html");
            String domId = req.params("domId");
            if (domId != null && !domId.trim().isEmpty()) {
                DomSnapshot domSnapshot = deviceService.getDomSnapshots().get(domId);
                if (domSnapshot != null) {
                    return domSnapshot.getOriginSource();
                } else {
                    return "Can't find DOM snapshot for key: " + domId;
                }
            } else throw new RuntimeException("Incorrect request, missing domId");
        });

        post("/api/tester/test", (request, response) -> {
            TestCommand testCommand = mapper.readValue(request.body(), TestCommand.class);
            testerService.runtTest(testCommand);
            return "Started testing: " + testCommand.getSpecPath();
        });

        get("/api/tester/results", (request, response) -> {
            response.header("Content-Type", APPLICATION_JSON);
            return testResultService.getTestResultsOverview();
        }, toJson());

        get("/api/files", (req, res) -> {
            return fileBrowserService.getFilesInPath(".");
        }, toJson());
        get("/api/files/", (req, res) -> {
            return fileBrowserService.getFilesInPath(".");
        }, toJson());
        get("/api/files/*", (req, res) -> {
            String[] splat = req.splat();
            if (splat.length > 0) {
                return fileBrowserService.getFilesInPath(splat[0]);
            } else throw new RuntimeException("Incorrect request");
        }, toJson());
        get("/api/file-content/*", (req, res) -> {
            String[] splat = req.splat();
            if (splat.length > 0) {
                return fileBrowserService.showFileContent(splat[0]);
            } else throw new RuntimeException("Incorrect request");
        }, toJson());
        put("/api/file-content/*", (req, res) -> {
            String[] splat = req.splat();
            if (splat.length > 0) {
                FileContent fileContent = mapper.readValue(req.body(), FileContent.class);
                fileBrowserService.saveFile(splat[0], fileContent);
                return "saved";
            } else throw new RuntimeException("Incorrect request");
        }, toJson());

        get("api/devices", (request, response) -> deviceService.getAllDevices(), toJson());

        post("api/devices", (req, res) -> {
            DeviceRequest createDeviceRequest = mapper.readValue(req.body(), DeviceRequest.class);
            deviceService.createDevice(createDeviceRequest);
            return "created";
        });

        put("api/devices/:deviceId", (req, res) -> {
            String deviceId = req.params("deviceId");
            if (deviceId != null && !deviceId.trim().isEmpty()) {
                DeviceRequest createDeviceRequest = mapper.readValue(req.body(), DeviceRequest.class);
                deviceService.changeDevice(deviceId, createDeviceRequest);
                return "modified";
            } else throw new RuntimeException("Incorrect request, missing device id");
        });

        delete("api/devices/:deviceId", (req, res) -> {
            String deviceId = req.params("deviceId");
            if (deviceId != null && !deviceId.trim().isEmpty()) {
                deviceService.shutdownDevice(deviceId);
                return "Delete device " + deviceId;
            } else throw new RuntimeException("Incorrect request, missing device id");
        }, toJson());

        get("api/settings", (request, response) -> settingsService.getSettings(), toJson());

        post("api/settings", (request, response) -> {
            Settings settings = mapper.readValue(request.body(), Settings.class);
            settingsService.changeSettings(settings);
            return "saved";
        });

        get("api/profiles", (req, res) -> {
            return profilesService.getProfiles();
        }, toJson());

        post("api/profiles", (req, res) -> {
            SaveProfileRequest saveProfileRequest = mapper.readValue(req.body(), SaveProfileRequest.class);
            profilesService.saveProfile(saveProfileRequest.getName());
            return "saved";
        }, toJson());

        post("api/profiles-load/*", (req, res) -> {
            String[] splat = req.splat();
            if (splat.length > 0) {
                profilesService.loadProfile(splat[0]);
                return "loaded";
            } else throw new RuntimeException("Incorrect request");
        }, toJson());
    }
}
