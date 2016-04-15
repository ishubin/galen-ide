/*******************************************************************************
* Copyright 2016 Ivan Shubin http://galenframework.com
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*   http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
package com.galenframework.ide.devices;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class Device {
    private String deviceId;

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

    public Device() {
    }

    public Device(String deviceId, String name, String browserType, List<String> tags, SizeProvider sizeProvider) {
        this(deviceId, name, browserType, tags, sizeProvider, DeviceStatus.STARTING);
    }

    public Device(String deviceId, String name, String browserType, List<String> tags, SizeProvider sizeProvider, DeviceStatus status) {
        this.deviceId = deviceId;
        this.name = name;
        this.tags = tags;
        this.browserType = browserType;
        this.sizeProvider = sizeProvider;
        this.status = status;
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

    public Device setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
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

    public Dimension retrieveCurrentSize() {
        return this.driver.manage().window().getSize();
    }

    public void shutdown() {
        this.isActive = false;
        if (this.driver != null) {
            this.driver.quit();
        }
    }
}
