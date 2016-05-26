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
package com.galenframework.ide.tests.integration.controllers;

import com.galenframework.ide.devices.*;
import com.galenframework.ide.model.Size;
import com.galenframework.ide.model.SizeVariation;
import com.galenframework.ide.model.devices.DeviceRequest;
import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.tests.integration.components.api.Response;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class DeviceControllerIT extends ApiTestBase {
    DeviceService deviceService = registerMock(DeviceService.class);

    @Test
    public void should_get_list_of_devices() throws IOException {
        when(deviceService.getAllDevices())
            .thenReturn(asList(
                new Device("qwe", "Device1", "firefox", singletonList("mobile"), new SizeProviderCustom(asList(new Size(450, 500))), DeviceStatus.BUSY),
                new Device("asd", "Device2", "chrome", singletonList("tablet"), new SizeProviderUnsupported(), DeviceStatus.STARTING),
                new Device("zxc", "Device3", "ie", singletonList("desktop"), new SizeProviderRange(new SizeVariation(new Size(1000, 700), new Size(1200, 900), 6, true)), DeviceStatus.READY)
            ));

        Response response = getJson("/api/devices");

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), jsonFromResources("/json/expected-get-devices-response.json"));
        verify(deviceService).getAllDevices();
    }

    @Test
    public void should_post_device() throws IOException {
        Response response = postJson("/api/devices",
            jsonFromResources("/json/post-device-request-1.json"));

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), "created");
        verify(deviceService).createDevice(eq(new DeviceRequest()
            .setName("Device1")
            .setBrowserType("firefox")
            .setSizes(singletonList(new Size(450, 500)))
            .setMaster(false)
            .setSizeType("custom")
            .setTags(singletonList("mobile")))
        );
    }

    @Test
    public void should_post_master_device() throws IOException {
        Response response = postJson("/api/devices",
            jsonFromResources("/json/post-master-device-request-1.json"));

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), "created");
        verify(deviceService).createDevice(eq(new DeviceRequest()
                .setName("Device1")
                .setBrowserType("firefox")
                .setSizes(singletonList(new Size(450, 500)))
                .setMaster(true)
                .setSizeType("custom")
                .setTags(singletonList("mobile")))
        );
    }

}
