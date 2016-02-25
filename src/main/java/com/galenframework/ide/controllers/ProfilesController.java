package com.galenframework.ide.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galenframework.ide.SaveProfileRequest;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.profiles.ProfilesService;
import static com.galenframework.ide.JsonTransformer.toJson;
import static spark.Spark.*;

public class ProfilesController {

    private final ProfilesService profilesService;
    ObjectMapper mapper = new ObjectMapper();

    public ProfilesController(ProfilesService profilesService) {
        this.profilesService = profilesService;
        initRoutes();
    }

    public void initRoutes() {
        get("api/profiles", (req, res) -> {
            return profilesService.getProfiles(new RequestData(req));
        }, toJson());

        post("api/profiles", (req, res) -> {
            SaveProfileRequest saveProfileRequest = mapper.readValue(req.body(), SaveProfileRequest.class);
            profilesService.saveProfile(new RequestData(req), saveProfileRequest.getName());
            return "saved";
        }, toJson());

        post("api/profiles-load/*", (req, res) -> {
            String[] splat = req.splat();
            if (splat.length > 0) {
                profilesService.loadProfile(new RequestData(req), splat[0]);
                return "loaded";
            } else throw new RuntimeException("Incorrect request");
        }, toJson());

    }
}
