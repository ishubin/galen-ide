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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galenframework.ide.DeviceRequest;
import com.galenframework.ide.controllers.actions.*;
import com.galenframework.ide.devices.commands.DeviceCommand;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.devices.DeviceService;
import org.openqa.selenium.Dimension;

import java.util.Optional;

import static com.galenframework.ide.JsonTransformer.toJson;
import static spark.Spark.*;

public class DeviceController {

    private final DeviceService deviceService;
    private ObjectMapper mapper = new ObjectMapper();
    private final String reportStoragePath;

    public DeviceController(DeviceService deviceService, String reportFolderStorage) {
        this.deviceService = deviceService;
        this.reportStoragePath = reportFolderStorage;
        initRoutes();
    }

    public void initRoutes() {
        get("api/devices", (request, response) -> deviceService.getAllDevices(new RequestData(request)), toJson());

        post("api/devices", (req, res) -> {
            DeviceRequest createDeviceRequest = mapper.readValue(req.body(), DeviceRequest.class);
            deviceService.createDevice(new RequestData(req), createDeviceRequest);
            return "created";
        });

        get("api/devices/:deviceId", (req, res) -> deviceService.getDevice(new RequestData(req), req.params("deviceId")), toJson());

        put("api/devices/:deviceId", (req, res) -> {
            String deviceId = req.params("deviceId");
            if (deviceId != null && !deviceId.trim().isEmpty()) {
                DeviceRequest createDeviceRequest = mapper.readValue(req.body(), DeviceRequest.class);
                deviceService.changeDevice(new RequestData(req), deviceId, createDeviceRequest);
                return "modified";
            } else throw new RuntimeException("Incorrect request, missing device id");
        });

        delete("api/devices/:deviceId", (req, res) -> {
            String deviceId = req.params("deviceId");
            if (deviceId != null && !deviceId.trim().isEmpty()) {
                deviceService.shutdownDevice(new RequestData(req), deviceId);
                return "Delete device " + deviceId;
            } else throw new RuntimeException("Incorrect request, missing device id");
        }, toJson());


        post("api/devices/:deviceId/actions/:actionName", (req, res) -> {
            String deviceId = req.params("deviceId");
            String actionName = req.params("actionName");
            String requestBody = req.body();

            DeviceAction deviceAction = DeviceAction.parseAction(actionName, requestBody);
            Optional<Object> result = deviceAction.execute(new RequestData(req), deviceService, deviceId, reportStoragePath);
            if (result.isPresent()) {
                return result;
            } else {
                return "registered action: " + actionName;
            }
        }, toJson());

        get("api/devices/:deviceId/actions", (req, res) -> {
            String deviceId = req.params("deviceId");
            return deviceService.getCurrentCommands(new RequestData(req), deviceId);
        }, toJson());
    }
}
