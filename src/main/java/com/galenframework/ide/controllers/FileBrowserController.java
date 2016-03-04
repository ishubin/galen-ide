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
