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
import com.galenframework.ide.model.profile.SaveProfileRequest;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.profiles.ProfilesService;
import com.galenframework.ide.services.settings.SettingsService;

import java.io.File;

import static com.galenframework.ide.util.JsonTransformer.toJson;
import static spark.Spark.*;

public class ProfilesController {
    public static final String GALEN_EXTENSION = ".galen";

    private final ProfilesService profilesService;
    private final SettingsService settingsService;
    ObjectMapper mapper = new ObjectMapper();

    public ProfilesController(ProfilesService profilesService, SettingsService settingsService) {
        this.profilesService = profilesService;
        this.settingsService = settingsService;
        initRoutes();
    }

    public void initRoutes() {
        get("api/profiles", (req, res) -> {
            RequestData requestData = new RequestData(req);
            String fullPath = obtainRootFolder(requestData).getAbsolutePath();
            return profilesService.getProfiles(requestData, fullPath);
        }, toJson());

        post("api/profiles", (req, res) -> {
            SaveProfileRequest saveProfileRequest = mapper.readValue(req.body(), SaveProfileRequest.class);
            RequestData requestData = new RequestData(req);

            String name = saveProfileRequest.getName();

            if (name == null || name.trim().isEmpty()) {
                throw new RuntimeException("Name should not be empty");
            }
            String fileName = name.replace("/", "");
            if (!fileName.endsWith(GALEN_EXTENSION)) {
                fileName = fileName + GALEN_EXTENSION;
            }

            String fullPath = obtainRootFolder(requestData).getAbsolutePath() + File.separator + fileName;
            profilesService.saveProfile(requestData, fullPath);
            return "saved";
        }, toJson());

        post("api/profiles-load/*", (req, res) -> {
            String[] splat = req.splat();
            if (splat.length > 0) {
                RequestData requestData = new RequestData(req);
                String fullPath = obtainRootFolder(requestData).getAbsolutePath() + File.separator + splat[0];
                profilesService.loadProfile(requestData, fullPath);
                return "loaded";
            } else throw new RuntimeException("Incorrect request");
        }, toJson());

    }

    private File obtainRootFolder(RequestData requestData) {
        File root = new File(settingsService.getSettings(requestData).getHomeDirectory());
        if (root.exists()) {
            if (!root.isDirectory()) {
                throw new RuntimeException("Home is not a directory: " + root.getAbsolutePath());
            }
        } else {
            if (!root.mkdirs()) {
                throw new RuntimeException("Cannot create a directory: " + root.getAbsolutePath());
            }
        }
        return root;
    }
}
