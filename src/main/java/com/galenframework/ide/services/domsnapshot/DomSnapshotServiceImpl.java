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
package com.galenframework.ide.services.domsnapshot;

import com.galenframework.ide.model.DomSnapshot;
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
