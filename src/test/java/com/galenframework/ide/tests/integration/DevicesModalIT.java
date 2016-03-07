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
package com.galenframework.ide.tests.integration;

import com.galenframework.ide.DeviceRequest;
import com.galenframework.ide.Size;
import com.galenframework.ide.TestResultContainer;
import com.galenframework.ide.TestResultsOverview;
import com.galenframework.ide.devices.Device;
import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.services.filebrowser.FileBrowserService;
import com.galenframework.ide.services.results.TestResultService;
import com.galenframework.ide.tests.integration.components.TestDevice;
import com.galenframework.ide.tests.integration.components.pages.IdePage;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

public class DevicesModalIT extends GalenTestBase {
    FileBrowserService fileBrowserService = registerMock(FileBrowserService.class);
    DeviceService deviceService = registerMock(DeviceService.class);
    TestResultService testResultService = registerMock(TestResultService.class);


    @Test
    public void addNewDeviceModal_shouldLookGood() {
        configureDefaultMocks();

        onEveryDevice(device -> {
            IdePage page = new IdePage(getDriver()).waitForIt();
            page.devicesPanel.addNewDeviceLink.click();
            page.deviceModal.waitForIt();

            checkLayout("/specs/tests/add-new-device-modal.gspec", device.getTags());
        });
    }

    @Test
    public void allSizeProvidersPanels_shouldLookGood() {
        configureDefaultMocks();

        onEveryDevice(device -> {
            IdePage page = new IdePage(getDriver()).waitForIt();
            page.devicesPanel.addNewDeviceLink.click();
            page.deviceModal.waitForIt();

            for (String sizeProviderType: asList("custom", "range", "unsupported")) {
                page.deviceModal.chooseSizeProvider(sizeProviderType);

                checkLayout("/specs/tests/device_modal_only_size_provider.gspec", device.getTags(), new HashMap<String, Object>() {{
                    put("size_provider", sizeProviderType);
                }});
            }
        });
    }

    @Test(dataProvider = "allDevices")
    public void whenAddingNewDevice_itShouldInvoke_deviceService_createDevice(TestDevice device) {
        resizeFor(device);
        configureDefaultMocks();
        loadDefaultTestUrl();

        IdePage page = new IdePage(getDriver()).waitForIt();
        page.devicesPanel.addNewDeviceLink.click();
        page.deviceModal.waitForIt();

        page.deviceModal.name.typeText("Sample device");
        page.deviceModal.browser.selectByText("Chrome");
        page.deviceModal.tags.clear().typeText("desktop, tablet");
        page.deviceModal.customSizeProvider.sizes.clear().typeText("800x700, 900x700, 1024x768");

        page.deviceModal.submitButton.click();
        page.deviceModal.waitUntilHidden();

        verify(deviceService, atLeastOnce()).getAllDevices(any());
        verify(deviceService).createDevice(any(), eq(new DeviceRequest()
                .setName("Sample device")
                .setBrowserType("chrome")
                .setTags(asList("desktop", "tablet"))
                .setSizeType("custom")
                .setSizes(asList(new Size(800, 700), new Size(900, 700), new Size(1024, 768)))
        ));
        verifyNoMoreInteractions(deviceService);
    }


    private void configureDefaultMocks() {
        when(fileBrowserService.getFilesInPath(any(), any())).thenReturn(Collections.emptyList());
        when(deviceService.getAllDevices(any())).thenReturn(Collections.<Device>emptyList());
        when(testResultService.getTestResultsOverview(any())).thenReturn(new TestResultsOverview(Collections.<TestResultContainer>emptyList(), null));
    }
}
