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
package com.galenframework.ide.devices.commands;

import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.DeviceThread;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DeviceOpenUrlCommand extends DeviceCommand {
    private final String url;

    public DeviceOpenUrlCommand(String url) {
        this.url = url;
    }

    @Override
    public void execute(Device device, DeviceThread deviceThread) {
        System.out.println("Opening " + url);
        device.getDriver().get(url);
    }

    @Override
    public String getName() {
        return "openUrl";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("url", url)
                .toString();
    }
}
