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

import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.SizeProviderCustom;
import com.galenframework.ide.devices.SizeProviderRange;
import com.galenframework.ide.model.Size;
import com.galenframework.ide.model.SizeVariation;
import com.galenframework.ide.model.devices.DeviceRequest;
import com.galenframework.ide.model.results.TestResultsOverview;
import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.services.filebrowser.FileBrowserService;
import com.galenframework.ide.services.filebrowser.FileItem;
import com.galenframework.ide.services.results.TestResultService;
import com.galenframework.ide.tests.integration.components.pages.IdePage;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.*;

public class DevicesModalIT extends GalenTestBase {
    private static final List<Device> SAMPLE_DEVICE_LIST = asList(
            new Device("123qweasd", "Mobile device", "firefox", asList("mobile", "iphone"), new SizeProviderCustom(asList(new Size(450, 700), new Size(500, 700)))),
            new Device("zxcvbnm", "Tablet device", "chrome", asList("tablet"), new SizeProviderRange(new SizeVariation(new Size(700, 800), new Size(900, 800), 10, false)))
    );
    private static final TestResultsOverview EMPTY_TEST_RESULTS = new TestResultsOverview(emptyList(), null);
    private static final List<FileItem> EMPTY_FILES = emptyList();
    private static final List<Device> EMPTY_DEVICES = emptyList();

    FileBrowserService fileBrowserService = registerMockitoMock(FileBrowserService.class);
    DeviceService deviceService = registerMockitoMock(DeviceService.class);
    TestResultService testResultService = registerMockitoMock(TestResultService.class);


    @Test
    public void addNewDeviceModal_shouldLookGood() throws IOException {
        configureInitialMockCalls(EMPTY_FILES, EMPTY_DEVICES, EMPTY_TEST_RESULTS);

        IdePage page = new IdePage(getDriver()).waitForIt();
        page.devicesPanel.addNewDeviceLink.click();
        page.deviceModal.waitForIt();

        checkLayout("/specs/tests/add-new-device-modal.gspec");
    }

    @Test
    public void allSizeProvidersPanels_shouldLookGood() throws IOException {
        configureInitialMockCalls(EMPTY_FILES, EMPTY_DEVICES, EMPTY_TEST_RESULTS);

        IdePage page = new IdePage(getDriver()).waitForIt();
        page.devicesPanel.addNewDeviceLink.click();
        page.deviceModal.waitForIt();

        for (String sizeProviderType: asList("custom", "range", "unsupported")) {
            page.deviceModal.chooseSizeProvider(sizeProviderType);

            checkLayout("/specs/tests/device_modal_only_size_provider.gspec", emptyList(), new HashMap<String, Object>() {{
                put("size_provider", sizeProviderType);
            }});
        }
    }

    @Test
    public void whenAddingNewDevice_itShouldInvoke_deviceService_createDevice() {
        configureInitialMockCalls(EMPTY_FILES, EMPTY_DEVICES, EMPTY_TEST_RESULTS);
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

        verify(deviceService, atLeastOnce()).getAllDevices();
        verify(deviceService).createDevice(eq(new DeviceRequest()
                .setName("Sample device")
                .setBrowserType("chrome")
                .setTags(asList("desktop", "tablet"))
                .setSizeType("custom")
                .setSizes(asList(new Size(800, 700), new Size(900, 700), new Size(1024, 768)))
        ));
        verifyNoMoreInteractions(deviceService);
    }

    @Test
    public void tableWithDevices_shouldLookGood() throws InterruptedException, IOException {
        configureInitialMockCalls(EMPTY_FILES, SAMPLE_DEVICE_LIST, EMPTY_TEST_RESULTS);

        loadDefaultTestUrl();
        IdePage page = new IdePage(getDriver()).waitForIt();
        page.devicesPanel.devices.get(0).editButton.click();
        page.deviceModal.waitForIt();

        checkLayout("/specs/tests/edit-device-modal.gspec");
    }

    @Test
    public void whenEditingDevice_itShouldInvoke_deviceService_changeDevice() throws InterruptedException {
        configureInitialMockCalls(EMPTY_FILES, SAMPLE_DEVICE_LIST, EMPTY_TEST_RESULTS);

        loadDefaultTestUrl();
        IdePage page = new IdePage(getDriver()).waitForIt();
        page.devicesPanel.devices.get(0).editButton.click();
        page.deviceModal.waitForIt();

        page.deviceModal.name.clear().typeText("Changed device name");
        page.deviceModal.submitButton.click();
        page.deviceModal.waitUntilHidden();

        verify(deviceService, atLeastOnce()).getAllDevices();
        verify(deviceService).changeDevice(eq("123qweasd"), eq(new DeviceRequest()
                .setName("Changed device name")
                .setBrowserType("firefox")
                .setTags(asList("mobile", "iphone"))
                .setSizeType("custom")
                .setSizes(asList(new Size(450, 700), new Size(500, 700)))
        ));
        verifyNoMoreInteractions(deviceService);
    }

    protected void configureInitialMockCalls(List<FileItem> fileBrowserServiceReturn,
                                             List<Device> deviceServiceReturn,
                                             TestResultsOverview testResultsServiceReturn) {
        when(fileBrowserService.getFilesInPath(any())).thenReturn(fileBrowserServiceReturn);
        when(deviceService.getAllDevices()).thenReturn(deviceServiceReturn);
        when(testResultService.getTestResultsOverview()).thenReturn(testResultsServiceReturn);
    }
}
