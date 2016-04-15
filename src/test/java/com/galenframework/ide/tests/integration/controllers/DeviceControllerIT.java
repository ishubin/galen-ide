package com.galenframework.ide.tests.integration.controllers;

import com.galenframework.ide.devices.*;
import com.galenframework.ide.model.Size;
import com.galenframework.ide.model.SizeVariation;
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
        when(deviceService.getAllDevices(any()))
            .thenReturn(asList(
                new Device("qwe", "Device1", "firefox", singletonList("mobile"), new SizeProviderCustom(asList(new Size(450, 500))), DeviceStatus.BUSY),
                new Device("asd", "Device2", "chrome", singletonList("tablet"), new SizeProviderUnsupported(), DeviceStatus.STARTING),
                new Device("zxc", "Device3", "ie", singletonList("desktop"), new SizeProviderRange(new SizeVariation(new Size(1000, 700), new Size(1200, 900), 6, true)), DeviceStatus.READY)
            ));

        Response response = getJson("/api/devices");

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), jsonFromResources("/json/expected-get-devices-response.json"));
        verify(deviceService).getAllDevices(any());
    }


}
