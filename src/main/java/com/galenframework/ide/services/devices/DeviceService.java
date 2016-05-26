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

import com.galenframework.ide.model.devices.DeviceRequest;
import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.commands.DeviceCommand;
import com.galenframework.ide.services.Service;
import org.openqa.selenium.Dimension;

import java.util.List;

public interface DeviceService extends  Service {

    void createDevice(DeviceRequest createDeviceRequest);

    List<Device> getAllDevices();

    Device getDevice(String deviceId);

    void syncAllBrowsersWithMaster();

    void testAllNodeDevices(String spec, String reportStoragePath);

    void shutdownDevice(String deviceId);

    void changeDevice(String deviceId, DeviceRequest deviceRequest);

    void shutdownAllDevices();

    /**
     * Opens url on specified device
     * @param deviceId Id of device
     * @param url Url of the website that will be open
     */
    void openUrl(String deviceId, String url);

    String checkLayout(String deviceId, String specPath, List<String> tags, String reportStoragePath);

    void resize(String deviceId, Dimension size);

    List<DeviceCommand> getCurrentCommands(String deviceId);

    void injectScript(String deviceId, String script);

    void runJavaScript(String deviceId, String path);

    void restartDevice(String deviceId);

    void clearCookies(String deviceId);
}
