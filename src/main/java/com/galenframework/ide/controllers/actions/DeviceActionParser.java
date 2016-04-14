package com.galenframework.ide.controllers.actions;

public interface DeviceActionParser {

    public DeviceAction parse(String requestBody) throws Exception;
}
