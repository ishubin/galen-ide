package com.galenframework.ide.tests.integration.mocks.services;

import com.galenframework.ide.Settings;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.settings.SettingsService;

public class DefaultSettingsServiceMock implements SettingsService {
    @Override
    public Settings getSettings(RequestData requestData) {
        return new Settings();
    }

    @Override
    public void changeSettings(RequestData requestData, Settings settings) {
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return null;
    }
}
