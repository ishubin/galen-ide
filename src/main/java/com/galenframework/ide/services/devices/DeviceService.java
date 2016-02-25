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
import com.galenframework.ide.DomSnapshot;
import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.TestResultsListener;
import com.galenframework.ide.services.Service;

import java.util.List;
import java.util.Map;

public interface DeviceService extends  Service {

    void createDevice(DeviceRequest createDeviceRequest);

    List<Device> getAllDevices();

    void syncAllBrowsers();

    void testAllBrowsers(String spec, String reportStoragePath);

    Map<String, DomSnapshot> getDomSnapshots();

    void shutdownDevice(String deviceId);

    void changeDevice(String deviceId, DeviceRequest deviceRequest);

    void shutdownAllDevices();
}
