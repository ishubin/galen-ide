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
package com.galenframework.ide.tests.integration.mocks.stubs;

import com.galenframework.ide.model.devices.DeviceRequest;
import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.commands.DeviceCommand;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.devices.DeviceService;
import org.openqa.selenium.Dimension;

import java.util.Collections;
import java.util.List;

public class DefaultDeviceServiceStub implements DeviceService {
    @Override
    public void createDevice(DeviceRequest createDeviceRequest) {
    }

    @Override
    public List<Device> getAllDevices() {
        return Collections.emptyList();
    }

    @Override
    public Device getDevice(String deviceId) {
        return null;
    }

    @Override
    public void updateAllPages(String pageUrl, String domSyncMethod) {
    }

    @Override
    public void testAllNodeDevices(String spec, String reportStoragePath) {
    }

    @Override
    public void shutdownDevice(String deviceId) {
    }

    @Override
    public void changeDevice(String deviceId, DeviceRequest deviceRequest) {
    }

    @Override
    public void shutdownAllDevices() {
    }

    @Override
    public void openUrl(String deviceId, String url) {

    }

    @Override
    public String checkLayout(String deviceId, String specPath, List<String> tags, String reportStoragePath) {
        return null;
    }

    @Override
    public void resize(String deviceId, Dimension size) {

    }

    @Override
    public List<DeviceCommand> getCurrentCommands(String deviceId) {
        return Collections.emptyList();
    }

    @Override
    public void injectScript(String deviceId, String script) {

    }

    @Override
    public String runJavaScript(String deviceId, String path) {
        return null;
    }

    @Override
    public void restartDevice(String deviceId) {

    }

    @Override
    public void clearCookies(String deviceId) {

    }

    @Override
    public ServiceProvider getServiceProvider() {
        return null;
    }
}