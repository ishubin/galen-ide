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
import com.galenframework.ide.devices.Device;
import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.services.filebrowser.FileBrowserService;
import com.galenframework.ide.services.filebrowser.FileItem;
import com.galenframework.ide.services.profiles.ProfilesService;
import com.galenframework.ide.services.results.TestResultService;
import com.galenframework.ide.tests.integration.components.pages.IdePage;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Collections;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

public class ProfilesModalIT extends GalenTestBase {
    FileBrowserService fileBrowserService = registerMock(FileBrowserService.class);
    DeviceService deviceService = registerMock(DeviceService.class);
    TestResultService testResultService = registerMock(TestResultService.class);
    ProfilesService profilesService = registerMock(ProfilesService.class);

    @Test
    public void loadProfilesModal_isDisplayedCorrectly() throws InterruptedException, IOException {
        configureDefaultMocks();

        IdePage page = new IdePage(getDriver()).waitForIt();
        page.header.loadProfileLink.click();
        page.loadProfilesModal.waitForIt();

        checkLayout("/specs/tests/load-profiles-modal.gspec");
    }

    @Test
    public void whenClickingTheProfile_itShouldInvoke_profileService_loadProfile() throws InterruptedException {
        configureDefaultMocks();
        loadDefaultTestUrl();

        IdePage page = new IdePage(getDriver()).waitForIt();
        page.header.loadProfileLink.click();
        page.loadProfilesModal.waitForIt();
        page.loadProfilesModal.profiles.get(1).click();
        page.loadProfilesModal.waitUntilHidden();

        verify(profilesService).loadProfile(any(), eq("profile-2.gspec"));
    }


    private void configureDefaultMocks() {
        when(fileBrowserService.getFilesInPath(any(), any())).thenReturn(Collections.emptyList());
        when(deviceService.getAllDevices(any())).thenReturn(Collections.<Device>emptyList());
        when(testResultService.getTestResultsOverview(any())).thenReturn(new TestResultsOverview(Collections.<TestResultContainer>emptyList(), null));
        when(profilesService.getProfiles(any())).thenReturn(asList(
                new FileItem(false, "profile-1.gspec", "somepath/profile-1.gspec", false),
                new FileItem(false, "profile-2.gspec", "somepath/profile-2.gspec", false),
                new FileItem(false, "profile-3.gspec", "somepath/profile-3.gspec", false)
        ));
    }

}