package com.galenframework.ide.services.domsnapshot;

import com.galenframework.ide.DomSnapshot;
import com.galenframework.ide.services.ServiceImpl;
import com.galenframework.ide.services.ServiceProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DomSnapshotServiceImpl extends ServiceImpl implements DomSnapshotService {

    private Map<String, DomSnapshot> domSnapshots = new HashMap<>();

    public DomSnapshotServiceImpl(ServiceProvider serviceProvider) {
        super(serviceProvider);
    }

    @Override
    public Optional<DomSnapshot> getDomSnapshot(String snapshotId) {
        return Optional.of(domSnapshots.get(snapshotId));
    }

    @Override
    public String createSnapshot(String originSource, String url) {
        String uniqueDomId = UUID.randomUUID().toString();
        try {
            domSnapshots.put(uniqueDomId, DomSnapshot.createSnapshotAndReplaceUrls(originSource, url));
        } catch (Exception ex) {
            throw new RuntimeException("Couldn't parse url: " + url, ex);
        }
        return uniqueDomId;
    }
}
