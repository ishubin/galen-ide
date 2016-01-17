package com.galenframework.quicktester.devices;

import org.openqa.selenium.WebDriver;

public class DeviceOpenUrlCommand extends DeviceCommand {
    private final String url;

    public DeviceOpenUrlCommand(String url) {
        this.url = url;
    }

    @Override
    public void execute(Device device, WebDriver driver) {
        driver.get(url);
    }
}
