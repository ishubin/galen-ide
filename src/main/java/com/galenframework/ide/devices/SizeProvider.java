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

import com.galenframework.ide.DeviceRequest;
import org.openqa.selenium.Dimension;

import java.util.function.Consumer;

public abstract class SizeProvider {
    public abstract String getType();

    public static SizeProvider readFrom(DeviceRequest createDeviceRequest) {
        switch (createDeviceRequest.getSizeType()) {
            case "custom":
                return new SizeProviderCustom(createDeviceRequest.getSizes());
            case "range":
                return new SizeProviderRange(createDeviceRequest.getSizeVariation());
            case "unsupported":
                return new SizeProviderUnsupported();
            default:
                throw new RuntimeException("Unknown size type: " + createDeviceRequest.getSizeType());
        }
    }

    public abstract void forEachIteration(DeviceThread deviceThread, Consumer<Dimension> action);
}
