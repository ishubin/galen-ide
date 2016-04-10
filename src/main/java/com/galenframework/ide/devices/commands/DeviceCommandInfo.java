package com.galenframework.ide.devices.commands;

public class DeviceCommandInfo {
    private String name;

    public DeviceCommandInfo(DeviceCommand command) {
        name = command.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
