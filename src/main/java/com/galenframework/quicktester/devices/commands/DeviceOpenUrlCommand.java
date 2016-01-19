package com.galenframework.quicktester.devices.commands;

import com.galenframework.quicktester.devices.Device;
import com.galenframework.quicktester.devices.DeviceThread;

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
