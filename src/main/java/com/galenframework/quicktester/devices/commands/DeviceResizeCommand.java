package com.galenframework.quicktester.devices.commands;

import com.galenframework.quicktester.devices.Device;
import com.galenframework.quicktester.devices.DeviceThread;
import com.galenframework.utils.GalenUtils;
import org.openqa.selenium.Dimension;

public class DeviceResizeCommand extends DeviceCommand {
    private final Dimension size;

    public DeviceResizeCommand(Dimension size) {
        this.size = size;
    }

    @Override
    public void execute(Device device, DeviceThread deviceThread) {
        GalenUtils.resizeDriver(device.getDriver(), size.width, size.height);
    }
}
