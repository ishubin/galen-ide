package com.galenframework.ide.controllers.actions;

import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.devices.DeviceService;

import java.util.Optional;

public class DeviceActionInject implements DeviceAction {
    private final DeviceInjectRequest injectRequest;

    public DeviceActionInject(DeviceInjectRequest injectRequest) {
        this.injectRequest = injectRequest;
    }

    @Override
    public Optional<Object> execute(RequestData requestData, DeviceService deviceService, String deviceId, String reportStoragePath) {
        deviceService.injectScript(requestData, deviceId, injectRequest.getScript());
        return Optional.empty();
    }
}
