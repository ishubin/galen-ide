package com.galenframework.quicktester.devices;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class Device {
    private List<String> tags;
    private String name;
    private String icon;

    @JsonIgnore
    private WebDriver driver;
    private List<Dimension> sizes;
    private boolean isActive = true;

    private DeviceStatus status = DeviceStatus.STARTING;

    public Device(String name, String icon, List<String> tags, List<Dimension> sizes) {
        this.name = name;
        this.sizes = sizes;
        this.tags = tags;
        this.icon = icon;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public List<Dimension> getSizes() {
        return sizes;
    }

    public void setSizes(List<Dimension> sizes) {
        this.sizes = sizes;
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
}
