package com.galenframework.ide.services.settings;

import com.galenframework.ide.Settings;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.ServiceProvider;

public class SettingsServiceImpl implements SettingsService {

    private final ServiceProvider serviceProvider;
    private Settings settings = new Settings();

    public SettingsServiceImpl(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public Settings getSettings(RequestData requestData) {
        return settings;
    }

    @Override
    public void changeSettings(RequestData requestData, Settings settings) {
        if (settings == null) {
            throw new IllegalArgumentException("Settings should not be null");
        }
        this.settings = settings;
        applySystemPropertyIfDefined("webdriver.chrome.driver", settings.getChromeDriverBinPath());
        applySystemPropertyIfDefined("phantomjs.binary.path", settings.getPhantomjsDriverBinPath());
        applySystemPropertyIfDefined("webdriver.edge.driver", settings.getEdgeDriverBinPath());
        applySystemPropertyIfDefined("webdriver.ie.driver", settings.getIeDriverBinPath());
    }

    private void applySystemPropertyIfDefined(String systemPropertyName, String value) {
        if (value != null) {
            System.setProperty(systemPropertyName, value);
        }
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }
}
