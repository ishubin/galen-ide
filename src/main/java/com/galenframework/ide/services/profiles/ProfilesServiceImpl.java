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
package com.galenframework.ide.services.profiles;

import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.ServiceProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galenframework.ide.model.devices.DeviceRequest;
import com.galenframework.ide.model.profile.ProfileContent;
import com.galenframework.ide.services.filebrowser.FileItem;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.galenframework.ide.controllers.ProfilesController.GALEN_EXTENSION;


public class ProfilesServiceImpl implements ProfilesService {
    private final ServiceProvider serviceProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProfilesServiceImpl(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public List<FileItem> getProfiles(RequestData requestData, String folderPath) {
        File[] filesInFolder = new File(folderPath).listFiles();

        List<FileItem> fileItems = new LinkedList<>();
        if (filesInFolder != null) {
            for (File file : filesInFolder) {
                if (file.getName().endsWith(GALEN_EXTENSION)) {
                    fileItems.add(FileItem.createFrom(file));
                }
            }
        }
        return fileItems;
    }


    @Override
    public void saveProfile(RequestData requestData, String path) {


        File profileFile = new File(path);
        ProfileContent profileContent = new ProfileContent();
        profileContent.setSettings(serviceProvider.settingsService().getSettings(requestData));
        profileContent.setDevices(serviceProvider.deviceService().getAllDevices(requestData).stream().map(DeviceRequest::fromDevice).collect(Collectors.toList()));

        try {
            FileUtils.writeStringToFile(profileFile, objectMapper.writeValueAsString(profileContent));
        } catch (IOException e) {
            throw new RuntimeException("Error during saving profile", e);
        }
    }

    @Override
    public void loadProfile(RequestData requestData, String path) {
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            try {
                String content = FileUtils.readFileToString(file);
                ProfileContent profileContent = objectMapper.readValue(content, ProfileContent.class);
                if (profileContent != null) {
                    loadProfile(requestData, profileContent);
                }
            } catch (Exception e) {
                throw new RuntimeException("Couldn't read file:" + path, e);
            }
        } else {
            throw new RuntimeException("Cannot find file: " + path);
        }
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }


    private void loadProfile(RequestData requestData, ProfileContent profileContent) {
        serviceProvider.settingsService().changeSettings(requestData, profileContent.getSettings());
        serviceProvider.deviceService().shutdownAllDevices(requestData);

        List<DeviceRequest> deviceRequests = profileContent.getDevices();
        if (deviceRequests != null) {
            deviceRequests.stream().forEach(dr ->
                serviceProvider.deviceService().createDevice(requestData, dr)
            );
        }
    }

}
