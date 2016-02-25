package com.galenframework.ide.services;

import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.services.devices.DeviceServiceImpl;
import com.galenframework.ide.services.filebrowser.FileBrowserService;
import com.galenframework.ide.services.filebrowser.FileBrowserServiceImpl;
import com.galenframework.ide.services.profiles.ProfilesService;
import com.galenframework.ide.services.profiles.ProfilesServiceImpl;
import com.galenframework.ide.services.results.TestResultService;
import com.galenframework.ide.services.results.TestResultServiceImpl;
import com.galenframework.ide.services.settings.SettingsService;
import com.galenframework.ide.services.settings.SettingsServiceImpl;
import com.galenframework.ide.services.tester.TesterService;
import com.galenframework.ide.services.tester.TesterServiceImpl;

public class DefaultServiceProvider implements ServiceProvider {
    private final TesterServiceImpl testerService;
    private final DeviceService deviceService;
    private final FileBrowserService fileBrowserService;
    private final SettingsService settingsService;
    private final ProfilesService profilesService;
    private final TestResultService testResultService;

    public DefaultServiceProvider(String reportStoragePath) {
        this.testerService = new TesterServiceImpl(this, reportStoragePath);
        this.deviceService = new DeviceServiceImpl(this);
        this.fileBrowserService = new FileBrowserServiceImpl(this);
        this.settingsService = new SettingsServiceImpl(this);
        this.profilesService = new ProfilesServiceImpl(this);
        this.testResultService = new TestResultServiceImpl(this);
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
    public TestResultService testResultService() {
        return this.testResultService;
    }
}
