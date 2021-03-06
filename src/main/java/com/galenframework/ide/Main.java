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

import com.galenframework.ide.controllers.*;
import com.galenframework.ide.jobs.TaskResultsStorageCleanupJob;
import com.galenframework.ide.model.results.TaskResult;
import com.galenframework.ide.model.settings.IdeArguments;
import com.galenframework.ide.services.DefaultServiceProvider;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.util.SynchronizedStorage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.*;

import static spark.Spark.*;


public class Main {
    private final String reportFolder;
    private final String staticFolderForSpark;
    private final SynchronizedStorage<TaskResult> taskResultsStorage = new SynchronizedStorage<>();

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(7);

    protected Main(String fileStorage) throws IOException {
        if (fileStorage == null || fileStorage.trim().isEmpty()) {
            staticFolderForSpark = createTempReportFolder();
        } else {
            staticFolderForSpark = createFolder(fileStorage);
        }

        reportFolder = createFolder(staticFolderForSpark + File.separator + "reports");
    }

    public static void main(String[] args) throws IOException {
        IdeArguments ideArguments = IdeArguments.parse(args);
        Main main = new Main(ideArguments.getFileStorage());

        ServiceProvider serviceProvider = new DefaultServiceProvider(ideArguments, main.reportFolder, main.taskResultsStorage, main.scheduledExecutorService);
        main.initWebServer(serviceProvider, ideArguments);
    }

    protected void initWebServer(ServiceProvider serviceProvider, IdeArguments ideArguments) {
        port(ideArguments.getPort());
        staticFileLocation("/public");
        externalStaticFileLocation(staticFolderForSpark);
        System.out.println("Reports are in: " + reportFolder);

        new DeviceController(serviceProvider.deviceService());
        new DomSnapshotController(serviceProvider.domSnapshotService());
        new FileBrowserController(serviceProvider.fileBrowserService());
        new SettingsController(serviceProvider.settingsService());
        new ProfilesController(serviceProvider.profilesService(), serviceProvider.settingsService());
        new TaskResultController(serviceProvider.taskResultService());
        new TesterController(serviceProvider.testerService());
        new HelpController();

        scheduledExecutorService.scheduleAtFixedRate(
            new TaskResultsStorageCleanupJob(taskResultsStorage, ideArguments.getKeepLastResults(), ideArguments.getZombieResultsTimeout(), reportFolder),
            ideArguments.getCleanupPeriodInMinutes(),
            ideArguments.getCleanupPeriodInMinutes(),
            TimeUnit.MINUTES
        );

        if (ideArguments.getProfile() != null) {
            serviceProvider.profilesService().loadProfile(ideArguments.getProfile());
        }
    }


    private String createFolder(String path) throws IOException {
        FileUtils.forceMkdir(new File(path));
        return path;
    }

    private String createTempReportFolder() throws IOException {
        return Files.createTempDirectory("galen-ide-reports").toString();
    }

}
