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

import com.galenframework.ide.TestResultContainer;
import com.galenframework.ide.TestResultsOverview;
import com.galenframework.ide.devices.*;
import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.services.filebrowser.FileBrowserService;
import com.galenframework.ide.services.filebrowser.FileItem;
import com.galenframework.ide.services.results.TestResultService;
import org.testng.annotations.Test;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

public class MainPageIT extends GalenTestBase {

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

        onEveryTestDevice(device -> checkLayout("/specs/tests/initial-page.gspec", device.getTags()));
    }

}
