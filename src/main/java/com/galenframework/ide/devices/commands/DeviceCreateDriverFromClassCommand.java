package com.galenframework.ide.devices.commands;

import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.DeviceThread;
import org.openqa.selenium.WebDriver;

public class DeviceCreateDriverFromClassCommand extends DeviceCommand {
    private final Class<? extends WebDriver> driverClass;

    public DeviceCreateDriverFromClassCommand(Class<? extends WebDriver> driverClass) {
        this.driverClass = driverClass;
    }

    @Override
    public void execute(Device device, DeviceThread deviceThread) throws Exception {
        if (device.getDriver() != null) {
            try {
                device.getDriver().quit();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        WebDriver newDriver = driverClass.newInstance();
        device.setDriver(newDriver);
    }
}
