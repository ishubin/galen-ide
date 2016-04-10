package com.galenframework.ide.devices.commands;

import java.util.Date;

public class DeviceCommandInfo {
    private String name;
    private Date registeredAt;

    public DeviceCommandInfo(DeviceCommand command) {
        name = command.getName();
        registeredAt = command.getRegisteredAt();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
    }
}
