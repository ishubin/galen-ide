package com.galenframework.ide.tests.integration;

import com.galenframework.ide.TestResultContainer;
import com.galenframework.ide.TestResultsOverview;
import com.galenframework.ide.devices.Device;
import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.services.filebrowser.FileBrowserService;
import com.galenframework.ide.services.filebrowser.FileItem;
import com.galenframework.ide.services.results.TestResultService;
import org.testng.annotations.Test;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

public class InitialPageIT extends GalenTestBase {

    FileBrowserService fileBrowserService = registerMock(FileBrowserService.class);
    DeviceService deviceService = registerMock(DeviceService.class);
    TestResultService testResultService = registerMock(TestResultService.class);

    @Test
    public void initialPage_isDisplayedCorrectly() throws InterruptedException {
        when(fileBrowserService.getFilesInPath(any(), any())).thenReturn(asList(
                new FileItem(true, "folder1", "/folder1", false),
                new FileItem(false, "home.gspec", "/home.gspec", true)
        ));
        when(deviceService.getAllDevices(any())).thenReturn(Collections.<Device>emptyList());
        when(testResultService.getTestResultsOverview(any())).thenReturn(new TestResultsOverview(Collections.<TestResultContainer>emptyList(), null));

        onEveryDevice(device -> {
            checkLayout("/specs/tests/initial-page.gspec", device.getTags());
        });
    }

}
