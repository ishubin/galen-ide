package com.galenframework.ide.tests.integration;

import com.galenframework.ide.TestResultContainer;
import com.galenframework.ide.TestResultsOverview;
import com.galenframework.ide.devices.Device;
import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.services.filebrowser.FileBrowserService;
import com.galenframework.ide.services.results.TestResultService;
import com.galenframework.ide.tests.integration.components.pages.IdePage;
import org.testng.annotations.Test;

import java.util.Collections;

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

    private void configureDefaultMocks() {
        when(fileBrowserService.getFilesInPath(any(), any())).thenReturn(Collections.emptyList());
        when(deviceService.getAllDevices(any())).thenReturn(Collections.<Device>emptyList());
        when(testResultService.getTestResultsOverview(any())).thenReturn(new TestResultsOverview(Collections.<TestResultContainer>emptyList(), null));
    }
}
