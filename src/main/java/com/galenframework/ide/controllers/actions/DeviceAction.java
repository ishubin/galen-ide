package com.galenframework.ide.controllers.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.devices.DeviceService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface DeviceAction {

    Optional<Object> execute(RequestData requestData, DeviceService deviceService, String deviceId, String reportStoragePath);

    ObjectMapper mapper = new ObjectMapper();
    Map<String, DeviceActionParser> actionParsers = new HashMap<String, DeviceActionParser>(){{
        put("openUrl", requestBody -> new DeviceActionOpenUrl(mapper.readValue(requestBody, DeviceActionOpenUrlRequest.class)));
        put("resize", requestBody -> new DeviceActionResize(mapper.readValue(requestBody, DeviceActionResizeRequest.class)));
        put("checkLayout", requestBody -> new DeviceActionCheckLayout(mapper.readValue(requestBody, DeviceActionCheckLayoutRequest.class)));
        put("inject", requestBody -> new DeviceActionInject(mapper.readValue(requestBody, DeviceInjectRequest.class)));
        put("runJs", requestBody -> new DeviceActionRunJs(mapper.readValue(requestBody, DeviceRunJsRequest.class)));
    }};

    static DeviceAction parseAction(String actionName, String requestBody) throws Exception {
        if (actionParsers.containsKey(actionName)) {
            return actionParsers.get(actionName).parse(requestBody);
        } else {
            throw new RuntimeException("Unknown action: " + actionName);
        }
    }

}
