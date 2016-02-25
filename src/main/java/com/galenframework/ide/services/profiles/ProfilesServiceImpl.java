package com.galenframework.ide.services.profiles;

import com.galenframework.ide.services.devices.DeviceService;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.settings.SettingsService;
import com.galenframework.ide.services.tester.TesterService;
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

    public ProfilesServiceImpl(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public File obtainRootFolder() {
        File root = new File(serviceProvider.settingsService().getSettings().getHomeDirectory());
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

    @Override
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

    @Override
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
        profileContent.setSettings(serviceProvider.settingsService().getSettings());
        profileContent.setDevices(serviceProvider.deviceService().getAllDevices().stream().map(DeviceRequest::fromDevice).collect(Collectors.toList()));

        try {
            FileUtils.writeStringToFile(profileFile, objectMapper.writeValueAsString(profileContent));
        } catch (IOException e) {
            throw new RuntimeException("Error during saving profile", e);
        }
    }

    @Override
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
        serviceProvider.settingsService().changeSettings(profileContent.getSettings());
        serviceProvider.deviceService().shutdownAllDevices();

        List<DeviceRequest> deviceRequests = profileContent.getDevices();
        if (deviceRequests != null) {
            deviceRequests.stream().forEach(serviceProvider.deviceService()::createDevice);
        }
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }
}
