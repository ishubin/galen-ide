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
package com.galenframework.ide;


import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.SizeProviderCustom;
import com.galenframework.ide.devices.SizeProviderRange;
import com.galenframework.ide.devices.SizeProviderUnsupported;

import java.util.List;

public class DeviceRequest {
    private String browserType = "";
    private String name = "";
    private List<String> tags;
    private List<Size> sizes;
    private String sizeType;
    private SizeVariation sizeVariation;

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
    }

    public SizeVariation getSizeVariation() {
        return sizeVariation;
    }

    public void setSizeVariation(SizeVariation sizeVariation) {
        this.sizeVariation = sizeVariation;
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }

    public static DeviceRequest fromDevice(Device device) {
        DeviceRequest dr = new DeviceRequest();
        dr.setBrowserType(device.getBrowserType());
        dr.setTags(device.getTags());
        dr.setName(device.getName());
        dr.setSizeType(device.getSizeProvider().getType());
        if (device.getSizeProvider() instanceof SizeProviderCustom) {
            dr.setSizes(((SizeProviderCustom) device.getSizeProvider()).getSizes());
        } else if (device.getSizeProvider() instanceof SizeProviderRange) {
            dr.setSizeVariation(((SizeProviderRange) device.getSizeProvider()).getSizeVariation());
        }
        return dr;
    }
}
