package com.galenframework.ide.devices.commands;

import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.DeviceThread;

public class DeviceShutdownCommand extends DeviceCommand {
    @Override
    public void execute(Device device, DeviceThread deviceThread) throws Exception {
        if (device.getDriver() != null) {
            device.getDriver().quit();
            device.setIsActive(false);
        }
    }
}
