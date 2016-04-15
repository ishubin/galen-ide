package com.galenframework.ide.devices;

import com.galenframework.ide.devices.commands.DeviceCommand;

public class DeviceClearCookiesCommand extends DeviceCommand {
    @Override
    public void execute(Device device, DeviceThread deviceThread) throws Exception {
        device.getDriver().manage().deleteAllCookies();
    }

    @Override
    public String getName() {
        return "clearCookies";
    }
}
