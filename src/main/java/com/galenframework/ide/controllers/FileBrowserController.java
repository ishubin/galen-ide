package com.galenframework.ide.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.filebrowser.FileBrowserService;
import com.galenframework.ide.services.filebrowser.FileContent;

import static spark.Spark.*;
import static com.galenframework.ide.JsonTransformer.toJson;

public class FileBrowserController {

    private final FileBrowserService fileBrowserService;
    ObjectMapper mapper = new ObjectMapper();

    public FileBrowserController(FileBrowserService fileBrowserService) {
        this.fileBrowserService = fileBrowserService;
        initRoutes();
    }

    public void initRoutes() {
        get("/api/files", (req, res) -> {
            return fileBrowserService.getFilesInPath(new RequestData(req), ".");
        }, toJson());

        get("/api/files/", (req, res) -> {
            return fileBrowserService.getFilesInPath(new RequestData(req), ".");
        }, toJson());

        get("/api/files/*", (req, res) -> {
            String[] splat = req.splat();
            if (splat.length > 0) {
                return fileBrowserService.getFilesInPath(new RequestData(req), splat[0]);
            } else throw new RuntimeException("Incorrect request");
        }, toJson());

        get("/api/file-content/*", (req, res) -> {
            String[] splat = req.splat();
            if (splat.length > 0) {
                return fileBrowserService.showFileContent(new RequestData(req), splat[0]);
            } else throw new RuntimeException("Incorrect request");
        }, toJson());

        put("/api/file-content/*", (req, res) -> {
            String[] splat = req.splat();
            if (splat.length > 0) {
                FileContent fileContent = mapper.readValue(req.body(), FileContent.class);
                fileBrowserService.saveFile(new RequestData(req), splat[0], fileContent);
                return "saved";
            } else throw new RuntimeException("Incorrect request");
        }, toJson());
    }
}
