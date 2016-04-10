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
package com.galenframework.ide.services.devices;

import com.galenframework.ide.DeviceRequest;
import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.commands.DeviceCommandInfo;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.Service;
import org.openqa.selenium.Dimension;

import java.util.List;
import java.util.Map;

public interface DeviceService extends  Service {

    void createDevice(RequestData requestData, DeviceRequest createDeviceRequest);

    void startMasterDriver(RequestData requestData, DeviceRequest createDeviceRequest, String url);

    List<Device> getAllDevices(RequestData requestData);

    void syncAllBrowsers(RequestData requestData);

    void testAllBrowsers(RequestData requestData, String spec, String reportStoragePath);

    void shutdownDevice(RequestData requestData, String deviceId);

    void changeDevice(RequestData requestData, String deviceId, DeviceRequest deviceRequest);

    void shutdownAllDevices(RequestData requestData);

    /**
     * Opens url on specified device
     * @param requestData a request meta data
     * @param deviceId Id of device
     * @param url Url of the website that will be open
     */
    void openUrl(RequestData requestData, String deviceId, String url);

    String checkLayout(RequestData requestData, String deviceId, String specPath, List<String> tags, String reportStoragePath);

    void resize(RequestData requestData, String deviceId, Dimension size);

    List<DeviceCommandInfo> getCurrentCommands(RequestData requestData, String deviceId);
}
