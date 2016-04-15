package com.galenframework.ide.devices.commands;

import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.DeviceThread;

public class DeviceRestartCommand extends DeviceCommand {
    private final DeviceCommand deviceInitializationCommand;

    public DeviceRestartCommand(DeviceCommand deviceInitializationCommand) {
        super();
        this.deviceInitializationCommand = deviceInitializationCommand;
    }

    @Override
    public void execute(Device device, DeviceThread deviceThread) throws Exception {
        device.getDriver().quit();
        deviceInitializationCommand.execute(device, deviceThread);
    }

    @Override
    public String getName() {
        return "restart";
    }
}
