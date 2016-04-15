package com.galenframework.ide.tests.integration.controllers;

import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.tests.integration.components.api.Response;
import org.openqa.selenium.Dimension;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class DeviceActionsControllerIT extends ApiTestBase {

    DeviceService deviceService = registerMock(DeviceService.class);

    @Test
    public void should_post_openUrl_action() throws IOException {
        Response response = postJson("/api/devices/device01/actions/openUrl",
            "{\"url\":\"http://example.com/blah\"}"
        );

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), "\"registered action: openUrl\"");
        verify(deviceService).openUrl(any(), eq("device01"), eq("http://example.com/blah"));
    }

    @Test
    public void should_post_resize_action() throws IOException {
        Response response = postJson("/api/devices/device01/actions/resize",
            "{\"width\":600, \"height\": 450}"
        );

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), "\"registered action: resize\"");
        verify(deviceService).resize(any(), eq("device01"), eq(new Dimension(600, 450)));
    }

    @Test
    public void should_post_checkLayout_action() throws IOException {
        when(deviceService.checkLayout(any(), anyString(), anyString(), any(), anyString()))
            .thenReturn("some-report-id");

        Response response = postJson("/api/devices/device01/actions/checkLayout",
            "{\"path\":\"homepage.gspec\", \"tags\": [\"mobile\"]}"
        );

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), "{\"reportId\":\"some-report-id\"}");
        verify(deviceService).checkLayout(any(), eq("device01"), eq("homepage.gspec"), eq(singletonList("mobile")), any());
    }

    @Test
    public void should_post_inject_action() throws IOException {
        Response response = postJson("/api/devices/device01/actions/inject",
            "{\"script\": \"document.body.writeln('Hello');\"}"
        );

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), "\"registered action: inject\"");
        verify(deviceService).injectScript(any(), eq("device01"), eq("document.body.writeln('Hello');"));
    }

    @Test
    public void should_post_runJs_action() throws IOException {
        Response response = postJson("/api/devices/device01/actions/runJs",
            "{\"path\": \"somescript.js\"}"
        );

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), "\"registered action: runJs\"");
        verify(deviceService).runJavaScript(any(), eq("device01"), eq("somescript.js"));
    }

    @Test
    public void should_post_restart_action() throws IOException {
        Response response = postJson("/api/devices/device01/actions/restart", "");

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), "\"registered action: restart\"");
        verify(deviceService).restartDevice(any(), eq("device01"));
    }

    @Test
    public void should_post_clearCookies_action() throws IOException {
        Response response = postJson("/api/devices/device01/actions/clearCookies", "");

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), "\"registered action: clearCookies\"");
        verify(deviceService).clearCookies(any(), eq("device01"));
    }
}
