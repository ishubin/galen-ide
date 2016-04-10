package com.galenframework.ide.devices.commands;

import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.DeviceThread;
import com.galenframework.utils.GalenUtils;

public class DeviceInjectCommand extends DeviceCommand {

    private final String script;

    public DeviceInjectCommand(String script) {
        this.script = script;
    }

    @Override
    public void execute(Device device, DeviceThread deviceThread) throws Exception {
        GalenUtils.injectJavascript(device.getDriver(), script);
    }

    @Override
    public String getName() {
        return INJECT;
    }

    public String getScript() {
        return script;
    }
}
