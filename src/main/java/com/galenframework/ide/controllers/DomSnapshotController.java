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
import com.galenframework.ide.model.DomSnapshot;
import com.galenframework.ide.model.DomSnapshotRequest;
import com.galenframework.ide.services.domsnapshot.DomSnapshotService;

import java.util.Optional;

import static spark.Spark.*;

public class DomSnapshotController {

    private final DomSnapshotService domSnapshotService;
    ObjectMapper mapper = new ObjectMapper();

    public DomSnapshotController(DomSnapshotService domSnapshotService) {
        this.domSnapshotService = domSnapshotService;
        initRoutes();
    }

    public void initRoutes() {
        post("/api/dom-snapshots", (req, res) -> {
            String url = req.params().get("url");
            String source = req.params().get("source");
            if (url == null || source == null) {
                DomSnapshotRequest snapshotRequest = mapper.readValue(req.body(), DomSnapshotRequest.class);
                url = snapshotRequest.getUrl();
                source = snapshotRequest.getSource();
            }
            return domSnapshotService.createSnapshot(source, url);
        });

        get("/api/dom-snapshots/:domId/snapshot.html", (req, res) -> {
            res.header("Content-Type", "text/html");
            String domId = req.params("domId");
            if (domId != null && !domId.trim().isEmpty()) {
                Optional<DomSnapshot> domSnapshot = domSnapshotService.getDomSnapshot(domId);
                if (domSnapshot.isPresent()) {
                    return domSnapshot.get().getOriginSource();
                } else {
                    return "Can't find DOM snapshot for key: " + domId;
                }
            } else throw new RuntimeException("Incorrect request, missing domId");
        });
    }
}
