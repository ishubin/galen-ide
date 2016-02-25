package com.galenframework.ide.services.settings;

import com.galenframework.ide.Settings;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.Service;

public interface SettingsService extends Service {
    Settings getSettings(RequestData requestData);

    void changeSettings(RequestData requestData, Settings settings);

}
