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

import com.galenframework.ide.model.settings.Settings;
import com.galenframework.ide.model.results.TaskResultsOverview;
import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.services.filebrowser.FileBrowserService;
import com.galenframework.ide.services.filebrowser.FileItem;
import com.galenframework.ide.services.profiles.ProfilesService;
import com.galenframework.ide.services.results.TaskResultService;
import com.galenframework.ide.services.settings.SettingsService;
import com.galenframework.ide.tests.integration.components.pages.IdePage;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.*;

public class ProfilesModalIT extends GalenTestBase {
    FileBrowserService fileBrowserService = registerMockitoMock(FileBrowserService.class);
    DeviceService deviceService = registerMockitoMock(DeviceService.class);
    TaskResultService testResultService = registerMockitoMock(TaskResultService.class);
    SettingsService settingsService = registerMockitoMock(SettingsService.class);
    ProfilesService profilesService = registerMockitoMock(ProfilesService.class);

    private Settings settings = new Settings()
            .setHomeDirectory("target");

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

        String expectedPathPrefix = new File("target").getAbsolutePath() + File.separator;
        verify(profilesService).loadProfile(eq(expectedPathPrefix + "profile-2.gspec"));
    }


    private void configureDefaultMocks() {
        when(fileBrowserService.getFilesInPath(any())).thenReturn(emptyList());
        when(deviceService.getAllDevices()).thenReturn(emptyList());
        when(testResultService.getTestResultsOverview()).thenReturn(new TaskResultsOverview(emptyList()));
        when(settingsService.getSettings())
                .thenReturn(settings);
        when(profilesService.getProfiles(any())).thenReturn(asList(
                new FileItem(false, "profile-1.gspec", "somepath/profile-1.gspec", false),
                new FileItem(false, "profile-2.gspec", "somepath/profile-2.gspec", false),
                new FileItem(false, "profile-3.gspec", "somepath/profile-3.gspec", false)
        ));
    }

}
