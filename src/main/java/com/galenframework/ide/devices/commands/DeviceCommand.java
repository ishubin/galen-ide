package com.galenframework.ide.devices.commands;


import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.DeviceThread;

public abstract class DeviceCommand {
    public abstract void execute(Device device, DeviceThread deviceThread) throws Exception;
}

