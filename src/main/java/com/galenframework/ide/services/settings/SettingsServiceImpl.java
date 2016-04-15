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
package com.galenframework.ide.services.settings;

import com.galenframework.ide.model.settings.Settings;
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
