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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceRestartCommand extends DeviceCommand {
    private final static Logger logger = LoggerFactory.getLogger(DeviceExecutor.class);

    public DeviceRestartCommand() {
    }

    @Override
    public CommandExecutionResult execute(Device device, DeviceExecutor deviceExecutor, String taskId, Settings settings, String reportStoragePath) throws Exception {
        quitDriverSafely(device);
        if (deviceExecutor.getDeviceInitializationCommand() != null) {
            return deviceExecutor.getDeviceInitializationCommand().execute(device, deviceExecutor, taskId, settings, reportStoragePath);
        } else {
            return CommandExecutionResult.passed();
        }
    }

    private void quitDriverSafely(Device device) {
        try {
            device.getDriver().quit();
        } catch (Exception ex) {
            logger.error("Couldn't quit driver", ex);
        }
    }

    @Override
    public String getName() {
        return "restart";
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCommandId())
            .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        } else if(obj == this) {
            return true;
        } else if(!(obj instanceof DeviceRestartCommand)) {
            return false;
        } else {
            DeviceRestartCommand rhs = (DeviceRestartCommand) obj;
            return new EqualsBuilder()
                .append(rhs.getCommandId(), this.getCommandId())
                .isEquals();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("commandId", getCommandId())
            .toString();
    }
}
