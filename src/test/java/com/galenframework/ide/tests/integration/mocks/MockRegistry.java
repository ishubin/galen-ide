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
package com.galenframework.ide.tests.integration.mocks;

import com.galenframework.ide.services.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

public enum MockRegistry {
    INSTANCE;

    private Map<MockKey, Object> mocks = new HashMap<>();
    private Map<Object, Object> sessionLessMocks = new HashMap<>();
    private static Logger LOG = LoggerFactory.getLogger(MockRegistry.class);

    private <T> void register(String sessionId, T mock, String mockName) {
        MockKey mockKey = new MockKey(sessionId, mockName);
        mocks.put(mockKey, mock);
    }

    @SuppressWarnings("unchecked")
    private synchronized <T> Optional<T> pick(String sessionId, String mockName) {
        MockKey mockKey = new MockKey(sessionId, mockName);
        if (mocks.containsKey(mockKey)) {
            return Optional.ofNullable((T) mocks.get(mockKey));
        }
        return Optional.empty();
    }

    public static <T> void registerMock(String sessionId, T mock, String mockName) {
        LOG.info(format("Registering mock %s for sessionId %s", mockName, sessionId));
        MockRegistry.INSTANCE.register(sessionId, mock, mockName);
    }

    public static <T> Optional<T> pickMock(String sessionId, String mockName) {
        return MockRegistry.INSTANCE.pick(sessionId, mockName);
    }

    public static <T extends Service> Optional<T> pickSessionlessMock(String mockName) {
        return MockRegistry.INSTANCE.pickSessionless(mockName);
    }

    @SuppressWarnings("unchecked")
    private synchronized <T> Optional<T> pickSessionless(String mockName) {
        if (sessionLessMocks.containsKey(mockName)) {
            return Optional.ofNullable((T) sessionLessMocks.get(mockName));
        }
        return Optional.empty();
    }

    public static <T> void registerSessionlessMock(T mock, String mockName) {
        LOG.info(format("Registering sessionless mock %s", mockName));
        INSTANCE.sessionLessMocks.put(mockName, mock);
    }
}
