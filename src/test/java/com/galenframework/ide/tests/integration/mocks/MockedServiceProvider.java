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
package com.galenframework.ide.tests.integration.mocks;

import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.services.domsnapshot.DomSnapshotService;
import com.galenframework.ide.services.filebrowser.FileBrowserService;
import com.galenframework.ide.services.profiles.ProfilesService;
import com.galenframework.ide.services.results.TaskResultService;
import com.galenframework.ide.services.settings.SettingsService;
import com.galenframework.ide.services.tester.TesterService;

public class MockedServiceProvider implements ServiceProvider {

    @Override
    public TesterService testerService() {
        return new MockSessionBasedMockedService<>(TesterService.class).getService();
    }

    @Override
    public DeviceService deviceService() {
        return new MockSessionBasedMockedService<>(DeviceService.class).getService();
    }

    @Override
    public FileBrowserService fileBrowserService() {
        return new MockSessionBasedMockedService<>(FileBrowserService.class).getService();
    }

    @Override
    public SettingsService settingsService() {
        return new MockSessionBasedMockedService<>(SettingsService.class).getService();
    }

    @Override
    public ProfilesService profilesService() {
        return new MockSessionBasedMockedService<>(ProfilesService.class).getService();
    }

    @Override
    public TaskResultService taskResultService() {
        return new MockSessionBasedMockedService<>(TaskResultService.class).getService();
    }

    @Override
    public DomSnapshotService domSnapshotService() {
        return new MockSessionBasedMockedService<>(DomSnapshotService.class).getService();
    }
}
