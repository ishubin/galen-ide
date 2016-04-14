package com.galenframework.ide.controllers.actions;

import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.devices.DeviceService;

import java.util.Optional;

public class DeviceActionOpenUrl implements DeviceAction {
    private final DeviceActionOpenUrlRequest openUrlRequest;

    public DeviceActionOpenUrl(DeviceActionOpenUrlRequest openUrlRequest) {
        this.openUrlRequest = openUrlRequest;
    }

    @Override
    public Optional<Object> execute(RequestData requestData, DeviceService deviceService, String deviceId, String reportStoragePath) {
        deviceService.openUrl(requestData, deviceId, openUrlRequest.getUrl());
        return Optional.empty();
    }
}
