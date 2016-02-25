package com.galenframework.ide.services.profiles;

import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.ServiceProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galenframework.ide.DeviceRequest;
import com.galenframework.ide.ProfileContent;
import com.galenframework.ide.services.filebrowser.FileItem;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class ProfilesServiceImpl implements ProfilesService {
    public static final String GALEN_EXTENSION = ".galen";
    private final ServiceProvider serviceProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProfilesServiceImpl(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public List<FileItem> getProfiles(RequestData requestData) {
        File[] filesInFolder = obtainRootFolder(requestData).listFiles();

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
    public void saveProfile(RequestData requestData, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Name should not be empty");
        }
        String fileName = name.replace("/", "");

        if (!fileName.endsWith(GALEN_EXTENSION)) {
            fileName = fileName + GALEN_EXTENSION;
        }

        File profileFile = new File(obtainRootFolder(requestData).getAbsolutePath() + File.separator + fileName);
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
        File file = new File(obtainRootFolder(requestData).getAbsolutePath() + File.separator + path);
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

    private File obtainRootFolder(RequestData requestData) {
        File root = new File(serviceProvider.settingsService().getSettings(requestData).getHomeDirectory());
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
