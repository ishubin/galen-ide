package com.galenframework.ide.tests.integration.mocks;

import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.services.domsnapshot.DomSnapshotService;
import com.galenframework.ide.services.filebrowser.FileBrowserService;
import com.galenframework.ide.services.profiles.ProfilesService;
import com.galenframework.ide.services.results.TestResultService;
import com.galenframework.ide.services.settings.SettingsService;
import com.galenframework.ide.services.tester.TesterService;
import com.galenframework.ide.tests.integration.mocks.services.*;

public class MockedServiceProvider implements ServiceProvider {
    public static final String MOCK_KEY_COOKIE_NAME = "__MockUniqueKey__";

    @Override
    public TesterService testerService() {
        return new CookieBasedMockedService<>(TesterService.class, new DefaultTestServiceMock()).getService();
    }

    @Override
    public DeviceService deviceService() {
        return new CookieBasedMockedService<>(DeviceService.class, new DefaultDeviceServiceMock()).getService();
    }

    @Override
    public FileBrowserService fileBrowserService() {
        return new CookieBasedMockedService<>(FileBrowserService.class, new DefaultFileBrowserService()).getService();
    }

    @Override
    public SettingsService settingsService() {
        return new CookieBasedMockedService<>(SettingsService.class, new DefaultSettingsServiceMock()).getService();
    }

    @Override
    public ProfilesService profilesService() {
        return new CookieBasedMockedService<>(ProfilesService.class, new DefaultProfilesServiceMock()).getService();
    }

    @Override
    public TestResultService testResultService() {
        return new CookieBasedMockedService<>(TestResultService.class, new DefaultTestResultServiceMock()).getService();
    }

    @Override
    public DomSnapshotService domSnapshotService() {
        return new CookieBasedMockedService<>(DomSnapshotService.class, new DefaultDomSnapshotServiceMock()).getService();
    }
}
