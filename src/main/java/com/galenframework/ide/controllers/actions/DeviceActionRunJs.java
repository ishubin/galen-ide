package com.galenframework.ide.controllers.actions;

import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.devices.DeviceService;

import java.util.Optional;

public class DeviceActionRunJs implements DeviceAction {
    private final DeviceActionRunJsRequest runJsRequest;

    public DeviceActionRunJs(DeviceActionRunJsRequest runJsRequest) {
        this.runJsRequest = runJsRequest;
    }

    @Override
    public Optional<Object> execute(RequestData requestData, DeviceService deviceService, String deviceId, String reportStoragePath) {
        deviceService.runJavaScript(requestData, deviceId, runJsRequest.getPath());
        return Optional.empty();
    }
}
