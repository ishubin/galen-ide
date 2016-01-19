package com.galenframework.quicktester.devices.commands;


import com.galenframework.quicktester.devices.Device;
import com.galenframework.quicktester.devices.DeviceThread;

public abstract class DeviceCommand {
    public abstract void execute(Device device, DeviceThread deviceThread) throws Exception;
}

