package com.galenframework.quicktester.devices.commands;

import com.galenframework.quicktester.devices.Device;
import com.galenframework.quicktester.devices.DeviceThread;
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

        if (!device.getSizes().isEmpty()) {
            newDriver.manage().window().setSize(device.getSizes().get(0));
        } else {
            throw new RuntimeException("Should have at least one size");
        }
    }
}
