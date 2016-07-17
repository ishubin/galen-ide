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
package com.galenframework.ide.devices.tasks;

import com.galenframework.ide.devices.commands.DeviceCommand;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class DeviceTask {
    private String name;
    private String taskId;

    private List<DeviceCommand> commands;

    public DeviceTask() {
    }

    public DeviceTask(String name, List<DeviceCommand> commands) {
        this.name = name;
        this.commands = commands;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DeviceCommand> getCommands() {
        return commands;
    }

    public void setCommands(List<DeviceCommand> commands) {
        this.commands = commands;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(taskId)
            .append(name)
            .append(commands)
            .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        } else if(obj == this) {
            return true;
        } else if(!(obj instanceof DeviceTask)) {
            return false;
        } else {
            DeviceTask rhs = (DeviceTask) obj;
            return new EqualsBuilder()
                .append(rhs.taskId, this.taskId)
                .append(rhs.name, this.name)
                .append(rhs.commands, this.commands)
                .isEquals();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("taskId", taskId)
            .append("name", name)
            .append("commands", getCommands())
            .toString();
    }
}
