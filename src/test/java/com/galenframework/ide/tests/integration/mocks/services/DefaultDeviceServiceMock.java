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
package com.galenframework.ide.tests.integration.mocks.services;

import com.galenframework.ide.DeviceRequest;
import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.commands.DeviceCommand;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.devices.DeviceService;
import org.openqa.selenium.Dimension;

import java.util.Collections;
import java.util.List;

public class DefaultDeviceServiceMock implements DeviceService {
    @Override
    public void createDevice(RequestData requestData, DeviceRequest createDeviceRequest) {
    }

    @Override
    public void startMasterDriver(RequestData requestData, DeviceRequest createDeviceRequest, String url) {
    }

    @Override
    public List<Device> getAllDevices(RequestData requestData) {
        return Collections.emptyList();
    }

    @Override
    public Device getDevice(RequestData requestData, String deviceId) {
        return null;
    }

    @Override
    public void syncAllBrowsers(RequestData requestData) {
    }

    @Override
    public void testAllBrowsers(RequestData requestData, String spec, String reportStoragePath) {
    }

    @Override
    public void shutdownDevice(RequestData requestData, String deviceId) {
    }

    @Override
    public void changeDevice(RequestData requestData, String deviceId, DeviceRequest deviceRequest) {
    }

    @Override
    public void shutdownAllDevices(RequestData requestData) {
    }

    @Override
    public void openUrl(RequestData requestData, String deviceId, String url) {

    }

    @Override
    public String checkLayout(RequestData requestData, String deviceId, String specPath, List<String> tags, String reportStoragePath) {
        return null;
    }

    @Override
    public void resize(RequestData requestData, String deviceId, Dimension size) {

    }

    @Override
    public List<DeviceCommand> getCurrentCommands(RequestData requestData, String deviceId) {
        return Collections.emptyList();
    }

    @Override
    public void injectScript(RequestData requestData, String deviceId, String script) {

    }

    @Override
    public void runJavaScript(RequestData requestData, String deviceId, String path) {

    }

    @Override
    public void restartDevice(RequestData requestData, String deviceId) {

    }

    @Override
    public void clearCookies(RequestData requestData, String deviceId) {

    }

    @Override
    public ServiceProvider getServiceProvider() {
        return null;
    }
}
