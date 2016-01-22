package com.galenframework.ide.devices.commands;

import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.DeviceThread;

public class DeviceOpenUrlCommand extends DeviceCommand {
    private final String url;

    public DeviceOpenUrlCommand(String url) {
        this.url = url;
    }

    @Override
    public void execute(Device device, DeviceThread deviceThread) {
        System.out.println("Opening " + url);
        device.getDriver().get(url);
    }
}
