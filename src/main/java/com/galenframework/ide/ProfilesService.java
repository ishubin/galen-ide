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
package com.galenframework.ide;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galenframework.ide.devices.Device;
import com.galenframework.ide.devices.DeviceContainer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ProfilesService {
    public static final String GALEN_EXTENSION = ".galen";
    private final DeviceContainer deviceContainer;
    private final TesterService testerService;

    public ProfilesService(DeviceContainer deviceContainer, TesterService testerService) {
        this.deviceContainer = deviceContainer;
        this.testerService = testerService;
    }

    public File obtainRootFolder() {
        File root = new File(deviceContainer.getSettings().getHomeDirectory());
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
    public List<FileItem> getProfiles() {
        File[] filesInFolder = obtainRootFolder().listFiles();

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

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void saveProfile(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Name should not be empty");
        }
        String fileName = name.replace("/", "");

        if (!fileName.endsWith(GALEN_EXTENSION)) {
            fileName = fileName + GALEN_EXTENSION;
        }

        File profileFile = new File(obtainRootFolder().getAbsolutePath() + File.separator + fileName);
        ProfileContent profileContent = new ProfileContent();
        profileContent.setSettings(deviceContainer.getSettings());
        profileContent.setDevices(deviceContainer.getAllDevices().stream().map(DeviceRequest::fromDevice).collect(Collectors.toList()));

        try {
            FileUtils.writeStringToFile(profileFile, objectMapper.writeValueAsString(profileContent));
        } catch (IOException e) {
            throw new RuntimeException("Error during saving profile", e);
        }
    }

    public void loadProfile(String path) {
        File file = new File(obtainRootFolder().getAbsolutePath() + File.separator + path);
        if (file.exists() && !file.isDirectory()) {
            try {
                String content = FileUtils.readFileToString(file);
                ProfileContent profileContent = objectMapper.readValue(content, ProfileContent.class);
                if (profileContent != null) {
                    loadProfile(profileContent);
                }
            } catch (Exception e) {
                throw new RuntimeException("Couldn't read file:" + path, e);
            }
        } else {
            throw new RuntimeException("Cannot find file: " + path);
        }
    }

    private void loadProfile(ProfileContent profileContent) {
        deviceContainer.setSettings(profileContent.getSettings());
        deviceContainer.shutdownAllDevices();

        List<DeviceRequest> deviceRequests = profileContent.getDevices();
        if (deviceRequests != null) {
            deviceRequests.stream().forEach(testerService::createDevice);
        }
    }
}
