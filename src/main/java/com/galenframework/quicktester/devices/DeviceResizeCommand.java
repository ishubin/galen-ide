package com.galenframework.quicktester.devices;

import com.galenframework.utils.GalenUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

public class DeviceResizeCommand extends DeviceCommand {
    private final Dimension size;

    public DeviceResizeCommand(Dimension size) {
        this.size = size;
    }

    @Override
    public void execute(Device device, WebDriver driver) {
        GalenUtils.resizeDriver(driver, size.width, size.height);
    }
}
