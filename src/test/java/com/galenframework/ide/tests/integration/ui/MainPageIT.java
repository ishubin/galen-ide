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

import com.galenframework.ide.model.results.TestResultsOverview;
import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.services.filebrowser.FileBrowserService;
import com.galenframework.ide.services.filebrowser.FileItem;
import com.galenframework.ide.services.results.TestResultService;
import org.testng.annotations.Test;

import java.io.IOException;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.*;

public class MainPageIT extends GalenTestBase {

    FileBrowserService fileBrowserService = registerMockitoMock(FileBrowserService.class);
    DeviceService deviceService = registerMockitoMock(DeviceService.class);
    TestResultService testResultService = registerMockitoMock(TestResultService.class);

    @Test
    public void initialPage_isDisplayedCorrectly() throws InterruptedException, IOException {
        when(fileBrowserService.getFilesInPath(any())).thenReturn(asList(
                new FileItem(true, "folder1", "/folder1", false),
                new FileItem(false, "home.gspec", "/home.gspec", true)
        ));
        when(deviceService.getAllDevices()).thenReturn(emptyList());
        when(testResultService.getTestResultsOverview()).thenReturn(new TestResultsOverview(emptyList(), null));

        loadDefaultTestUrl();
        checkLayout("/specs/tests/initial-page.gspec");
    }

}
