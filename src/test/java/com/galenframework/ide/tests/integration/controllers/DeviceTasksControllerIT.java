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

import com.galenframework.ide.devices.commands.*;
import com.galenframework.ide.devices.tasks.DeviceTask;
import com.galenframework.ide.model.results.CommandResult;
import com.galenframework.ide.model.results.ExecutionStatus;
import com.galenframework.ide.model.results.TaskResult;
import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.tests.integration.components.api.Response;
import org.openqa.selenium.Dimension;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class DeviceTasksControllerIT extends ApiTestBase {

    DeviceService deviceService = registerMock(DeviceService.class);

    @Test
    public void should_post_openUrl_task() throws IOException {
        when(deviceService.executeTask(anyString(), anyObject())).thenReturn(
            new TaskResult("some-task-id", "some task", asList(new CommandResult("some-command-id", "openUrl", ExecutionStatus.planned)))
        );

        Response response = postJson("/api/devices/device01/tasks",
            "{\"name\":\"some task\",\"commands\":[{\"name\":\"openUrl\",\"parameters\":{\"url\":\"http://example.com/blah\"}}]}"
        );

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), expectedTaskResultJsonForSingleCommand("some-task-id", "some task", "some-command-id", "openUrl"));
        verify(deviceService).executeTask(eq("device01"),
            eq(new DeviceTask("some task", asList(
                new DeviceOpenUrlCommand("http://example.com/blah")
            )))
        );
    }

    @Test
    public void should_post_resize_action() throws IOException {
        when(deviceService.executeTask(anyString(), anyObject())).thenReturn(
            new TaskResult("some-task-id", "some task", asList(new CommandResult("some-command-id", "resize", ExecutionStatus.planned)))
        );

        Response response = postJson("/api/devices/device01/tasks",
            "{\"name\":\"some task\",\"commands\":[{\"name\":\"resize\",\"parameters\":{\"width\":1024,\"height\":768}}]}"
        );

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), expectedTaskResultJsonForSingleCommand("some-task-id", "some task", "some-command-id", "resize"));
        verify(deviceService).executeTask(eq("device01"),
            eq(new DeviceTask("some task", asList(
                new DeviceResizeCommand(1024, 768)
            )))
        );
    }

    @Test
    public void should_post_checkLayout_action() throws IOException {
        when(deviceService.executeTask(anyString(), anyObject())).thenReturn(
            new TaskResult("some-task-id", "some task", asList(new CommandResult("some-command-id", "checkLayout", ExecutionStatus.planned)))
        );

        Response response = postJson("/api/devices/device01/tasks",
            "{\"name\":\"some task\",\"commands\":[{\"name\":\"checkLayout\",\"parameters\":" +
                "{\"path\":\"homepage.gspec\", \"tags\": [\"mobile\"], \"vars\":{\"someStringVar\":\"some value\",\"someIntVar\":1234}}}]}"
        );

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), expectedTaskResultJsonForSingleCommand("some-task-id", "some task", "some-command-id", "checkLayout"));
        verify(deviceService).executeTask(eq("device01"),
            eq(new DeviceTask("some task", asList(
                new DeviceCheckLayoutCommand("homepage.gspec", asList("mobile"))
                .setVars(new HashMap<String, Object>() {{
                    put("someStringVar", "some value");
                    put("someIntVar", 1234);
                }})
            )))
        );
    }


    @Test
    public void should_post_inject_action() throws IOException {
        when(deviceService.executeTask(anyString(), anyObject())).thenReturn(
            new TaskResult("some-task-id", "some task", asList(new CommandResult("some-command-id", "inject", ExecutionStatus.planned)))
        );

        Response response = postJson("/api/devices/device01/tasks",
            "{\"name\":\"some task\",\"commands\":[{\"name\":\"inject\",\"parameters\":{\"script\": \"document.body.writeln('Hello');\"}}]}"
        );

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), expectedTaskResultJsonForSingleCommand("some-task-id", "some task", "some-command-id", "inject"));
        verify(deviceService).executeTask(eq("device01"),
            eq(new DeviceTask("some task", asList(
                new DeviceInjectCommand("document.body.writeln('Hello');")
            )))
        );
    }

    @Test
    public void should_post_runJs_action() throws IOException {
        when(deviceService.executeTask(anyString(), anyObject())).thenReturn(
            new TaskResult("some-task-id", "some task", asList(new CommandResult("some-command-id", "runJs", ExecutionStatus.planned)))
        );

        Response response = postJson("/api/devices/device01/tasks",
            "{\"name\":\"some task\",\"commands\":[{\"name\":\"runJs\",\"parameters\":{\"path\": \"some-script.js\"}}]}"
        );

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), expectedTaskResultJsonForSingleCommand("some-task-id", "some task", "some-command-id", "runJs"));
        verify(deviceService).executeTask(eq("device01"),
            eq(new DeviceTask("some task", asList(
                new DeviceRunJavaScriptCommand("some-script.js")
            )))
        );
    }

    @Test
    public void should_post_restart_action() throws IOException {
        when(deviceService.executeTask(anyString(), anyObject())).thenReturn(
            new TaskResult("some-task-id", "some task", asList(new CommandResult("some-command-id", "restart", ExecutionStatus.planned)))
        );

        Response response = postJson("/api/devices/device01/tasks",
            "{\"name\":\"some task\",\"commands\":[{\"name\":\"restart\",\"parameters\":{}}]}"
        );

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), expectedTaskResultJsonForSingleCommand("some-task-id", "some task", "some-command-id", "restart"));
        verify(deviceService).executeTask(eq("device01"),
            eq(new DeviceTask("some task", asList(
                new DeviceRestartCommand()
            )))
        );
    }


    @Test
    public void should_post_clearCookies_action() throws IOException {
        when(deviceService.executeTask(anyString(), anyObject())).thenReturn(
            new TaskResult("some-task-id", "some task", asList(new CommandResult("some-command-id", "clearCookies", ExecutionStatus.planned)))
        );

        Response response = postJson("/api/devices/device01/tasks",
            "{\"name\":\"some task\",\"commands\":[{\"name\":\"clearCookies\",\"parameters\":{}}]}"
        );

        assertEquals(response.getCode(), 200);
        assertEquals(response.getBody(), expectedTaskResultJsonForSingleCommand("some-task-id", "some task", "some-command-id", "clearCookies"));
        verify(deviceService).executeTask(eq("device01"),
            eq(new DeviceTask("some task", asList(
                new DeviceClearCookiesCommand()
            )))
        );
    }

    private String expectedTaskResultJsonForSingleCommand(String taskId, String taskName, String commandId, String commandName) {
        return "{\"name\":\"" + taskName + "\",\"taskId\":\"" + taskId + "\",\"status\":\"planned\",\"commands\":[" +
            "{\"status\":\"planned\",\"externalReport\":null,\"errorMessages\":null,\"externalReportFolder\":null,\"commandId\":\"" + commandId + "\",\"name\":\"" + commandName + "\"," +
            "\"startedDate\":null,\"finishedDate\":null}],\"startedDate\":null,\"finishedDate\":null}";
    }
}
