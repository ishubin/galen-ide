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
package com.galenframework.ide.controllers.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galenframework.ide.services.devices.DeviceService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface DeviceAction {

    Optional<Object> execute(DeviceService deviceService, String deviceId, String reportStoragePath);

    ObjectMapper mapper = new ObjectMapper();
    Map<String, DeviceActionParser> actionParsers = new HashMap<String, DeviceActionParser>(){{
        put("openUrl", requestBody -> new DeviceActionOpenUrl(mapper.readValue(requestBody, DeviceActionOpenUrlRequest.class)));
        put("resize", requestBody -> new DeviceActionResize(mapper.readValue(requestBody, DeviceActionResizeRequest.class)));
        put("checkLayout", requestBody -> new DeviceActionCheckLayout(mapper.readValue(requestBody, DeviceActionCheckLayoutRequest.class)));
        put("inject", requestBody -> new DeviceActionInject(mapper.readValue(requestBody, DeviceActionInjectRequest.class)));
        put("runJs", requestBody -> new DeviceActionRunJs(mapper.readValue(requestBody, DeviceActionRunJsRequest.class)));
        put("restart", requestBody -> new DeviceActionRestart());
        put("clearCookies", requestBody -> new DeviceActionClearCookies());
    }};

    static DeviceAction parseAction(String actionName, String requestBody) throws Exception {
        if (actionParsers.containsKey(actionName)) {
            return actionParsers.get(actionName).parse(requestBody);
        } else {
            throw new RuntimeException("Unknown action: " + actionName);
        }
    }

}
