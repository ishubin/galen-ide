package com.galenframework.ide.tests.integration.mocks.services;

import com.galenframework.ide.DeviceRequest;
import com.galenframework.ide.devices.Device;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.devices.DeviceService;

import java.util.Collections;
import java.util.List;

public class DefaultDeviceServiceMock implements DeviceService {
    @Override
    public void createDevice(RequestData requestData, DeviceRequest createDeviceRequest) {
    }

    @Override
    public List<Device> getAllDevices(RequestData requestData) {
        return Collections.emptyList();
    }

    @Override
    public void syncAllBrowsers(RequestData requestData) {
    }

    @Override
    public void testAllBrowsers(RequestData requestData, String spec, String reportStoragePath) {
    }

    @Override
    public void shutdownDevice(RequestData requestData, String deviceId) {
    }

    @Override
    public void changeDevice(RequestData requestData, String deviceId, DeviceRequest deviceRequest) {
    }

    @Override
    public void shutdownAllDevices(RequestData requestData) {
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return null;
    }
}
