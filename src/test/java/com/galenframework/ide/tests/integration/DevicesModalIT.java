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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;

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
