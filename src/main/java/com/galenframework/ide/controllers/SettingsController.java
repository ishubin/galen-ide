package com.galenframework.ide.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galenframework.ide.Settings;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.settings.SettingsService;
import static com.galenframework.ide.JsonTransformer.toJson;
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
