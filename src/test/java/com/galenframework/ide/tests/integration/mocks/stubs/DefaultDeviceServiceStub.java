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

import com.galenframework.ide.devices.tasks.DeviceTask;
import com.galenframework.ide.model.devices.DeviceRequest;
import com.galenframework.ide.devices.Device;
import com.galenframework.ide.model.results.TaskResult;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.devices.DeviceService;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

public class DefaultDeviceServiceStub implements DeviceService {
    @Override
    public void createDevice(DeviceRequest createDeviceRequest) {
    }

    @Override
    public List<Device> getAllDevices() {
        return emptyList();
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
    public List<DeviceTask> getCurrentTasks(String deviceId) {
        return emptyList();
    }

    @Override
    public void restartDevice(String deviceId) {

    }

    @Override
    public TaskResult executeTask(String deviceId, DeviceTask task) {
        return null;
    }


    @Override
    public ServiceProvider getServiceProvider() {
        return null;
    }
}
