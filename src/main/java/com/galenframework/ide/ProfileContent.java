package com.galenframework.ide;


import java.util.List;

public class ProfileContent {
    private Settings settings;
    private List<DeviceRequest> devices;

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Settings getSettings() {
        return settings;
    }

    public List<DeviceRequest> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceRequest> devices) {
        this.devices = devices;
    }
}
