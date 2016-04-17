package com.galenframework.ide.controllers.actions;

public class ActionResult {
    private final String actionName;
    private final String deviceId;
    private final Object result;

    public ActionResult(String actionName, String deviceId, Object result) {
        this.actionName = actionName;
        this.deviceId = deviceId;
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getActionName() {
        return actionName;
    }
}
