package com.galenframework.quicktester.devices;

import org.openqa.selenium.WebDriver;

public abstract class DeviceCommand {
    public abstract void execute(Device device, WebDriver driver);
}

