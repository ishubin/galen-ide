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
import com.galenframework.ide.devices.DeviceExecutor;
import com.galenframework.ide.model.results.CommandExecutionResult;
import com.galenframework.ide.model.settings.Settings;
import org.openqa.selenium.WebDriver;

import java.util.function.Supplier;

public class DeviceCreateDriverFromProvider extends DeviceCommand {
    private final Supplier<WebDriver> driverSupplier;

    public DeviceCreateDriverFromProvider(Supplier<WebDriver> driverSupplier) {
        super();
        this.driverSupplier = driverSupplier;
    }

    @Override
    public CommandExecutionResult execute(Device device, DeviceExecutor deviceExecutor, String taskId, Settings settings, String reportStoragePath) throws Exception {
        if (device.getDriver() != null) {
            try {
                device.getDriver().quit();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        device.setDriver(driverSupplier.get());
        return CommandExecutionResult.passed();
    }

    @Override
    public String getName() {
        return "createDriverWithProvider";
    }
}
