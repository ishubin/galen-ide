package com.galenframework.ide.tests.integration.mocks.services;

import com.galenframework.ide.DomSnapshot;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.domsnapshot.DomSnapshotService;

import java.util.Optional;

public class DefaultDomSnapshotServiceMock implements DomSnapshotService {
    @Override
    public Optional<DomSnapshot> getDomSnapshot(RequestData requestData, String snapshotId) {
        return Optional.empty();
    }

    @Override
    public String createSnapshot(RequestData requestData, String originSource, String url) {
        return "some-default-snapshot-id";
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return null;
    }
}
