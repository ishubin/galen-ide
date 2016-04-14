package com.galenframework.ide.controllers.actions;

import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.devices.DeviceService;
import org.openqa.selenium.Dimension;

import java.util.Optional;

public class DeviceActionResize implements DeviceAction {
    private final DeviceActionResizeRequest resizeRequest;

    public DeviceActionResize(DeviceActionResizeRequest resizeRequest) {
        this.resizeRequest = resizeRequest;
    }

    @Override
    public Optional<Object> execute(RequestData requestData, DeviceService deviceService, String deviceId, String reportStoragePath) {
        deviceService.resize(requestData, deviceId, new Dimension(resizeRequest.getWidth(), resizeRequest.getHeight()));
        return Optional.empty();
    }
}
