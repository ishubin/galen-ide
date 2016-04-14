package com.galenframework.ide.controllers.actions;

import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.devices.DeviceService;

import java.util.Optional;

public class DeviceActionCheckLayout implements DeviceAction {
    private final DeviceActionCheckLayoutRequest checkLayoutRequest;

    public DeviceActionCheckLayout(DeviceActionCheckLayoutRequest checkLayoutRequest) {
        this.checkLayoutRequest = checkLayoutRequest;
    }

    @Override
    public Optional<Object> execute(RequestData requestData, DeviceService deviceService, String deviceId, String reportStoragePath) {
        String reportId = deviceService.checkLayout(requestData, deviceId, checkLayoutRequest.getPath(), checkLayoutRequest.getTags(), reportStoragePath);
        return Optional.of(new DeviceActionCheckLayoutResponse(reportId));
    }
}
