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
package com.galenframework.ide.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galenframework.ide.model.settings.Settings;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.settings.SettingsService;
import static com.galenframework.ide.util.JsonTransformer.toJson;
import static spark.Spark.*;

public class SettingsController {

    private final SettingsService settingsService;
    ObjectMapper mapper = new ObjectMapper();

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
        initRoutes();
    }

    public void initRoutes() {
        get("api/settings", (request, response) -> settingsService.getSettings(new RequestData(request)), toJson());

        post("api/settings", (request, response) -> {
            Settings settings = mapper.readValue(request.body(), Settings.class);
            settingsService.changeSettings(new RequestData(request), settings);
            return "saved";
        });
    }
}
