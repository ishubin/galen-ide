package com.galenframework.ide;

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
        //launch(args);
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
        new TesterController(REPORT_FOLDER_STORAGE);
    }


    private String createFolder(String path) throws IOException {
        FileUtils.forceMkdir(new File(path));
        return path;
    }

    private String createTempReportFolder() throws IOException {
        return Files.createTempDirectory("galen-instant-tester-reports").toString();
    }

}
