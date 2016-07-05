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
package com.galenframework.ide.services;

import com.galenframework.ide.model.results.TaskResult;
import com.galenframework.ide.model.settings.IdeArguments;
import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.services.devices.DeviceServiceImpl;
import com.galenframework.ide.services.domsnapshot.DomSnapshotServiceImpl;
import com.galenframework.ide.services.domsnapshot.DomSnapshotService;
import com.galenframework.ide.services.filebrowser.FileBrowserService;
import com.galenframework.ide.services.filebrowser.FileBrowserServiceImpl;
import com.galenframework.ide.services.profiles.ProfilesService;
import com.galenframework.ide.services.profiles.ProfilesServiceImpl;
import com.galenframework.ide.services.results.TaskResultService;
import com.galenframework.ide.services.results.TaskResultServiceImpl;
import com.galenframework.ide.services.settings.SettingsService;
import com.galenframework.ide.services.settings.SettingsServiceImpl;
import com.galenframework.ide.services.tester.TesterService;
import com.galenframework.ide.services.tester.TesterServiceImpl;
import com.galenframework.ide.util.SynchronizedStorage;

import java.util.concurrent.ScheduledExecutorService;

public class DefaultServiceProvider implements ServiceProvider {
    private final TesterServiceImpl testerService;
    private final DeviceService deviceService;
    private final FileBrowserService fileBrowserService;
    private final SettingsService settingsService;
    private final ProfilesService profilesService;
    private final TaskResultService testResultService;
    private final DomSnapshotService domSnapshotService;

    public DefaultServiceProvider(IdeArguments ideArguments, String reportStoragePath, SynchronizedStorage<TaskResult> testResultsStorage, ScheduledExecutorService scheduledExecutorService) {
        this.testerService = new TesterServiceImpl(this, reportStoragePath);
        this.deviceService = new DeviceServiceImpl(ideArguments, this, reportStoragePath, scheduledExecutorService);
        this.fileBrowserService = new FileBrowserServiceImpl(this);
        this.settingsService = new SettingsServiceImpl(this);
        this.profilesService = new ProfilesServiceImpl(this);
        this.testResultService = new TaskResultServiceImpl(this, testResultsStorage);
        this.domSnapshotService = new DomSnapshotServiceImpl(this);
    }
    @Override
    public TesterService testerService() {
        return testerService;
    }

    @Override
    public DeviceService deviceService() {
        return this.deviceService;
    }

    @Override
    public FileBrowserService fileBrowserService() {
        return this.fileBrowserService;
    }

    @Override
    public SettingsService settingsService() {
        return this.settingsService;
    }

    @Override
    public ProfilesService profilesService() {
        return this.profilesService;
    }

    @Override
    public TaskResultService taskResultService() {
        return this.testResultService;
    }

    @Override
    public DomSnapshotService domSnapshotService() {
        return this.domSnapshotService;
    }
}
