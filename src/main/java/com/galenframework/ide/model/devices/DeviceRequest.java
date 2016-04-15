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
package com.galenframework.ide.model.devices;


import com.galenframework.ide.model.Size;
import com.galenframework.ide.model.SizeVariation;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    public DeviceRequest setBrowserType(String browserType) {
        this.browserType = browserType;
        return this;
    }

    public String getName() {
        return name;
    }

    public DeviceRequest setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getTags() {
        return tags;
    }

    public DeviceRequest setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public List<Size> getSizes() {
        return sizes;
    }

    public DeviceRequest setSizes(List<Size> sizes) {
        this.sizes = sizes;
        return this;
    }

    public SizeVariation getSizeVariation() {
        return sizeVariation;
    }

    public DeviceRequest setSizeVariation(SizeVariation sizeVariation) {
        this.sizeVariation = sizeVariation;
        return this;
    }

    public String getSizeType() {
        return sizeType;
    }

    public DeviceRequest setSizeType(String sizeType) {
        this.sizeType = sizeType;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.name)
                .append(this.browserType)
                .append(this.tags)
                .append(this.sizes)
                .append(this.sizeType)
                .append(this.sizeVariation)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        } else if(obj == this) {
            return true;
        } else if(!(obj instanceof DeviceRequest)) {
            return false;
        } else {
            DeviceRequest rhs = (DeviceRequest) obj;
            return new EqualsBuilder()
                    .append(this.name, rhs.name)
                    .append(this.browserType, rhs.browserType)
                    .append(this.tags, rhs.tags)
                    .append(this.sizes, rhs.sizes)
                    .append(this.sizeType, rhs.sizeType)
                    .append(this.sizeVariation, rhs.sizeVariation)
                    .isEquals();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", this.name)
                .append("browserType", this.browserType)
                .append("tags", this.tags)
                .append("sizes", this.sizes)
                .append("sizeType", this.sizeType)
                .append("sizeVariation", this.sizeVariation)
                .toString();
    }
}
