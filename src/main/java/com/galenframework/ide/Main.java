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

import com.galenframework.ide.services.DefaultServiceProvider;
import com.galenframework.ide.services.ServiceProvider;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static spark.Spark.*;


public class Main extends Application {

    public static void main(String[] args) throws IOException {
        new Main().initWebServer();
    }

    private final String REPORT_FOLDER_FOR_SPARK;
    private final String REPORT_FOLDER_STORAGE;

    public Main() throws IOException {
        REPORT_FOLDER_FOR_SPARK = createTempReportFolder();
        REPORT_FOLDER_STORAGE = createFolder(REPORT_FOLDER_FOR_SPARK + File.separator + "reports");
    }

    @Override
    public void start(Stage stage) throws Exception {
        initWebServer();
        stage.setTitle("Galen IDE");
        Scene scene = new Scene(new CustomBrowser(), 1024, 500, Color.web("#666970"));
        stage.setScene(scene);
        stage.show();
    }

    private void initWebServer() {
        staticFileLocation("/public");
        externalStaticFileLocation(REPORT_FOLDER_FOR_SPARK);
        System.out.println("Reports are in: " + REPORT_FOLDER_FOR_SPARK);
        ServiceProvider serviceProvider = new DefaultServiceProvider(REPORT_FOLDER_STORAGE);
        new TesterController(
                serviceProvider.deviceService(),
                serviceProvider.testerService(),
                serviceProvider.fileBrowserService(),
                serviceProvider.profilesService(),
                serviceProvider.settingsService(),
                serviceProvider.testResultService()
        );
    }


    private String createFolder(String path) throws IOException {
        FileUtils.forceMkdir(new File(path));
        return path;
    }

    private String createTempReportFolder() throws IOException {
        return Files.createTempDirectory("galen-instant-tester-reports").toString();
    }

}
