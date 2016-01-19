package com.galenframework.quicktester.devices.commands;

import com.galenframework.quicktester.devices.Device;
import com.galenframework.quicktester.devices.DeviceThread;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.JavascriptExecutor;

public class DeviceInjectSourceCommand extends DeviceCommand {
    private final String originSource;

    public DeviceInjectSourceCommand(String originSource) {
        this.originSource = originSource;
    }

    @Override
    public void execute(Device device, DeviceThread deviceThread) {

        String encodedBody = base64encode(originSource);
        ((JavascriptExecutor)device.getDriver()).executeScript("var source = arguments[0]; function injectBody() {document.write(atob(source))}; setTimeout(injectBody, 100);", encodedBody);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String base64encode(String originSource) {
        return new String(Base64.encodeBase64(originSource.getBytes()));
    }
}
