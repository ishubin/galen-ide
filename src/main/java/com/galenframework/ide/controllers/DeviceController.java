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
import com.galenframework.ide.devices.commands.DeviceCommand;
import com.galenframework.ide.devices.tasks.DeviceTaskParser;
import com.galenframework.ide.devices.tasks.DeviceTask;
import com.galenframework.ide.model.devices.DeviceRequest;
import com.galenframework.ide.services.devices.DeviceService;


import static com.galenframework.ide.util.JsonTransformer.toJson;
import static spark.Spark.*;

public class DeviceController {

    private final DeviceService deviceService;
    private ObjectMapper mapper = new ObjectMapper();

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
        initRoutes();
    }

    public void initRoutes() {
        get("api/devices", (request, response) -> deviceService.getAllDevices(), toJson());

        post("api/devices", (req, res) -> {
            DeviceRequest createDeviceRequest = mapper.readValue(req.body(), DeviceRequest.class);
            deviceService.createDevice(createDeviceRequest);
            return "created";
        });

        get("api/devices/:deviceId", (req, res) -> deviceService.getDevice(req.params("deviceId")), toJson());

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


        post("api/devices/:deviceId/tasks", (req, res) -> {
            String deviceId = req.params("deviceId");
            String requestBody = req.body();

            DeviceTask task = DeviceTaskParser.parseTask(mapper, mapper.readTree(requestBody));
            return deviceService.executeTask(deviceId, task);
        }, toJson());


        get("api/devices/:deviceId/tasks", (req, res) -> {
            String deviceId = req.params("deviceId");
            return deviceService.getCurrentTasks(deviceId);
        }, toJson());
    }


}
