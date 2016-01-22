package com.galenframework.ide.devices.commands;

import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.DeviceThread;
import com.galenframework.utils.GalenUtils;
import org.openqa.selenium.Dimension;

public class DeviceResizeCommand extends DeviceCommand {
    private final Dimension size;

    public DeviceResizeCommand(Dimension size) {
        this.size = size;
    }

    @Override
    public void execute(Device device, DeviceThread deviceThread) {
        if (device.isSupportsResizing()) {
            GalenUtils.resizeDriver(device.getDriver(), size.width, size.height);
        }
    }
}
