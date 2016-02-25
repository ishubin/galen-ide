package com.galenframework.ide.services;

import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.services.domsnapshot.DomSnapshotService;
import com.galenframework.ide.services.filebrowser.FileBrowserService;
import com.galenframework.ide.services.profiles.ProfilesService;
import com.galenframework.ide.services.results.TestResultService;
import com.galenframework.ide.services.settings.SettingsService;
import com.galenframework.ide.services.tester.TesterService;

public interface ServiceProvider {
    TesterService testerService();
    DeviceService deviceService();
    FileBrowserService fileBrowserService();
    SettingsService settingsService();
    ProfilesService profilesService();
    TestResultService testResultService();
    DomSnapshotService domSnapshotService();
}
