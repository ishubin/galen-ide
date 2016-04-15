package com.galenframework.ide.controllers.actions;

import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.devices.DeviceService;

import java.util.Optional;

public class DeviceActionRestart implements DeviceAction {
    @Override
    public Optional<Object> execute(RequestData requestData, DeviceService deviceService, String deviceId, String reportStoragePath) {
        deviceService.restartDevice(requestData, deviceId);
        return Optional.empty();
    }
}
