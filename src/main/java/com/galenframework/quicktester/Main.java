package com.galenframework.quicktester;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static spark.Spark.*;


public class Main {

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private final String REPORT_FOLDER_FOR_SPARK;
    private final String REPORT_FOLDER_STORAGE;

    public Main() throws IOException {
        REPORT_FOLDER_FOR_SPARK = createTempReportFolder();
        REPORT_FOLDER_STORAGE = createFolder(REPORT_FOLDER_FOR_SPARK + File.separator + "reports");
    }


    private String createFolder(String path) throws IOException {
        FileUtils.forceMkdir(new File(path));
        return path;
    }

    private String createTempReportFolder() throws IOException {
        return Files.createTempDirectory("galen-instant-tester-reports").toString();
    }

    private void run() {
        staticFileLocation("/public");
        externalStaticFileLocation(REPORT_FOLDER_FOR_SPARK);
        System.out.println("Reports are in: " + REPORT_FOLDER_FOR_SPARK);
        new TesterController(REPORT_FOLDER_STORAGE);
    }
}
