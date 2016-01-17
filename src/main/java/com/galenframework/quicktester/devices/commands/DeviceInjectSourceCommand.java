package com.galenframework.quicktester.devices.commands;

import com.galenframework.quicktester.devices.Device;
import com.galenframework.quicktester.devices.DeviceCommand;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class DeviceInjectSourceCommand extends DeviceCommand {
    private final String originSource;

    public DeviceInjectSourceCommand(String originSource) {
        this.originSource = originSource;
    }

    @Override
    public void execute(Device device, WebDriver driver) {
        String encodedBody = base64encode(originSource);
        ((JavascriptExecutor)driver).executeScript("var source = arguments[0]; function injectBody() {document.write(atob(source))}; setTimeout(injectBody, 100);", encodedBody);
        driver.findElement(By.tagName("body"));
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
