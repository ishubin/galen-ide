package com.galenframework.ide.devices;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.UUID;

public class Device {
    private String deviceId = UUID.randomUUID().toString();

    private List<String> tags;
    private String name;
    private boolean supportsResizing = true;

    @JsonIgnore
    private WebDriver driver;
    private SizeProvider sizeProvider;
    private boolean isActive = true;

    private DeviceStatus status = DeviceStatus.STARTING;
    private String lastErrorMessage;
    private String browserType;

    public Device(String name, String browserType, List<String> tags, SizeProvider sizeProvider) {
        this.name = name;
        this.tags = tags;
        this.browserType = browserType;
        this.sizeProvider = sizeProvider;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public void setLastErrorMessage(String lastErrorMessage) {
        this.lastErrorMessage = lastErrorMessage;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isSupportsResizing() {
        return supportsResizing;
    }

    public void setSupportsResizing(boolean supportsResizing) {
        this.supportsResizing = supportsResizing;
    }

    public SizeProvider getSizeProvider() {
        return sizeProvider;
    }

    public void setSizeProvider(SizeProvider sizeProvider) {
        this.sizeProvider = sizeProvider;
    }

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }
}
