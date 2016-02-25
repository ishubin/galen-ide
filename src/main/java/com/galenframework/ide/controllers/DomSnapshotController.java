package com.galenframework.ide.controllers;

import com.galenframework.ide.DomSnapshot;
import com.galenframework.ide.services.domsnapshot.DomSnapshotService;

import java.util.Optional;

import static spark.Spark.*;

public class DomSnapshotController {

    private final DomSnapshotService domSnapshotService;

    public DomSnapshotController(DomSnapshotService domSnapshotService) {
        this.domSnapshotService = domSnapshotService;
        initRoutes();
    }

    public void initRoutes() {
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
