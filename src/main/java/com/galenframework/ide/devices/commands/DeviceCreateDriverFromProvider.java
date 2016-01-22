package com.galenframework.ide.devices.commands;

import com.galenframework.ide.DriverProvider;
import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.DeviceThread;

public class DeviceCreateDriverFromProvider extends DeviceCommand {
    private final DriverProvider driverProvider;

    public DeviceCreateDriverFromProvider(DriverProvider driverProvider) {
        super();
        this.driverProvider = driverProvider;
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
        device.setDriver(driverProvider.provideDriver());
    }
}
