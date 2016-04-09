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
import com.galenframework.ide.services.DefaultServiceProvider;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.ServiceProvider;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static spark.Spark.*;


public class Main {

    protected final String REPORT_FOLDER_FOR_SPARK;
    protected final String REPORT_FOLDER_STORAGE;

    protected Main() throws IOException {
        REPORT_FOLDER_FOR_SPARK = createTempReportFolder();
        REPORT_FOLDER_STORAGE = createFolder(REPORT_FOLDER_FOR_SPARK + File.separator + "reports");
    }

    public static void main(String[] args) throws IOException {
        Main main = new Main();

        IdeArguments ideArguments = IdeArguments.parse(args);

        ServiceProvider serviceProvider = new DefaultServiceProvider(ideArguments, main.REPORT_FOLDER_STORAGE);
        main.initWebServer(serviceProvider, ideArguments, main.REPORT_FOLDER_STORAGE);
    }

    protected void initWebServer(ServiceProvider serviceProvider, IdeArguments ideArguments, String reportFolderStorage) {
        port(ideArguments.getPort());
        staticFileLocation("/public");
        externalStaticFileLocation(REPORT_FOLDER_FOR_SPARK);
        System.out.println("Reports are in: " + REPORT_FOLDER_FOR_SPARK);

        new DeviceController(serviceProvider.deviceService(), reportFolderStorage);
        new DomSnapshotController(serviceProvider.domSnapshotService());
        new FileBrowserController(serviceProvider.fileBrowserService());
        new SettingsController(serviceProvider.settingsService());
        new ProfilesController(serviceProvider.profilesService(), serviceProvider.settingsService());
        new TestResultController(serviceProvider.testResultService());
        new TesterController(serviceProvider.testerService());

        if (ideArguments.getProfile() != null) {
            serviceProvider.profilesService().loadProfile(RequestData.EMPTY, ideArguments.getProfile());
        }
    }


    private String createFolder(String path) throws IOException {
        FileUtils.forceMkdir(new File(path));
        return path;
    }

    private String createTempReportFolder() throws IOException {
        return Files.createTempDirectory("galen-instant-tester-reports").toString();
    }

}
