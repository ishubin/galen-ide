package com.galenframework.ide.devices.tasks;

import com.galenframework.ide.devices.commands.DeviceCommand;
import java.util.List;
import java.util.UUID;

public class DeviceTask {
    private String name;
    private String taskId = "task-" + UUID.randomUUID().toString();

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
}
