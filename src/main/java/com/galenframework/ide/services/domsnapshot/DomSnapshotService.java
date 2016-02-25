package com.galenframework.ide.services.domsnapshot;

import com.galenframework.ide.DomSnapshot;
import com.galenframework.ide.services.Service;

import java.util.Optional;

public interface DomSnapshotService extends Service {

    Optional<DomSnapshot> getDomSnapshot(String snapshotId);

    String createSnapshot(String originSource, String url);
}
