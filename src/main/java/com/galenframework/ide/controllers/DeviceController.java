package com.galenframework.ide.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galenframework.ide.DeviceRequest;
import com.galenframework.ide.services.devices.DeviceService;

import static com.galenframework.ide.JsonTransformer.toJson;
import static spark.Spark.*;

public class DeviceController {

    private final DeviceService deviceService;
    ObjectMapper mapper = new ObjectMapper();

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
    }
}
