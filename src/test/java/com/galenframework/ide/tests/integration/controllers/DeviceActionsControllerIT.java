package com.galenframework.ide.tests.integration.controllers;

import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.tests.integration.components.api.Response;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

public class DeviceActionsControllerIT extends ApiTestBase {


    DeviceService deviceService = registerMock(DeviceService.class);

    @Test
    public void should_post_openUrl_action() throws IOException {
        Response response = postJson("/api/devices/device01/actions/openUrl", "{\"url\":\"http://example.com/blah\"}");

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), "\"registered action: openUrl\"");

        verify(deviceService).openUrl(any(), eq("device01"), eq("http://example.com/blah"));
    }

}
