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

import com.galenframework.ide.services.devices.DeviceService;

import java.util.Optional;

public class DeviceActionRunJs implements DeviceAction {
    private final DeviceActionRunJsRequest runJsRequest;

    public DeviceActionRunJs(DeviceActionRunJsRequest runJsRequest) {
        this.runJsRequest = runJsRequest;
    }

    @Override
    public Optional<Object> execute(DeviceService deviceService, String deviceId, String reportStoragePath) {
        deviceService.runJavaScript(deviceId, runJsRequest.getPath());
        return Optional.empty();
    }
}
