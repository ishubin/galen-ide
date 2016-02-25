package com.galenframework.ide.services.domsnapshot;

import com.galenframework.ide.DomSnapshot;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.Service;

import java.util.Optional;

public interface DomSnapshotService extends Service {

    Optional<DomSnapshot> getDomSnapshot(RequestData requestData, String snapshotId);

    String createSnapshot(RequestData requestData, String originSource, String url);
}
