package com.galenframework.ide.devices.commands;

import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.DeviceThread;
import org.openqa.selenium.JavascriptExecutor;

public class DeviceInjectSourceCommand extends DeviceCommand {
    private final String originSource;

    public DeviceInjectSourceCommand(String originSource) {
        this.originSource = originSource;
    }

    @Override
    public void execute(Device device, DeviceThread deviceThread) {
        ((JavascriptExecutor)device.getDriver()).executeScript("var source = arguments[0]; function injectBody() {document.write(source)}; setTimeout(injectBody, 10);", originSource);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
