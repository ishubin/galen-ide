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
package com.galenframework.ide.tests.integration.ui;

import com.galenframework.ide.model.Size;
import com.galenframework.ide.model.SizeVariation;
import com.galenframework.ide.model.results.TaskResultsOverview;
import com.galenframework.ide.devices.*;
import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.services.filebrowser.FileBrowserService;
import com.galenframework.ide.services.results.TaskResultService;
import com.galenframework.ide.tests.integration.components.pages.IdePage;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

public class DevicesPanelIT extends GalenTestBase {
    FileBrowserService fileBrowserService = registerMockitoMock(FileBrowserService.class);
    DeviceService deviceService = registerMockitoMock(DeviceService.class);
    TaskResultService testResultService = registerMockitoMock(TaskResultService.class);

    private List<Device> sampleDevices = asList(
            new Device("id1", "Mobile device", "firefox", asList("mobile", "iphone"), new SizeProviderCustom(asList(new Size(450, 700), new Size(500, 700))), DeviceStatus.STARTING),
            new Device("id2", "Tablet device", "chrome", asList("tablet"), new SizeProviderRange(new SizeVariation(new Size(700, 800), new Size(900, 800), 10, false)), DeviceStatus.READY),
            new Device("id3", "Desktop device", "phantomjs", asList("desktop"), new SizeProviderUnsupported(), DeviceStatus.BUSY),
            new Device("id4", "Temp", "firefox", asList("desktop"), new SizeProviderUnsupported(), DeviceStatus.SHUTDOWN)
    );

    @Test
    public void table_with_devices_should_look_good() throws InterruptedException, IOException {
        configureMocks();

        loadDefaultTestUrl();
        checkLayout("/specs/tests/devices-panel.gspec");
    }

    @Test
    public void it_should_invoke_deviceService_shutdownDevice_only_when_clicking_active_delete_button() throws InterruptedException, IOException {
        configureMocks();
        loadDefaultTestUrl();

        IdePage page = new IdePage(getDriver()).waitForIt();
        String timeMarker = page.devicesPanel.getAttribute("data-generation-marker");
        page.devicesPanel.devices.get(3).deleteButton.click();
        page.devicesPanel.devices.get(1).deleteButton.click();
        page.devicesPanel.waitForAttributeToChange("data-generation-marker", timeMarker);

        verify(deviceService, atLeast(2)).getAllDevices();
        verify(deviceService).shutdownDevice(eq("id2"));
        verify(deviceService, never()).shutdownDevice(eq("id4"));
        verifyNoMoreInteractions(deviceService);
    }


    private void configureMocks() {
        when(fileBrowserService.getFilesInPath(any())).thenReturn(Collections.emptyList());
        when(deviceService.getAllDevices()).thenReturn(sampleDevices);
        when(testResultService.getTestResultsOverview()).thenReturn(new TaskResultsOverview(Collections.emptyList()));
    }

}
