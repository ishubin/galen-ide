package com.galenframework.ide.tests.integration.mocks.services;

import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.filebrowser.FileItem;
import com.galenframework.ide.services.profiles.ProfilesService;

import java.util.Collections;
import java.util.List;

public class DefaultProfilesServiceMock implements ProfilesService {
    @Override
    public List<FileItem> getProfiles(RequestData requestData) {
        return Collections.emptyList();
    }

    @Override
    public void saveProfile(RequestData requestData, String name) {
    }

    @Override
    public void loadProfile(RequestData requestData, String path) {
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return null;
    }
}
