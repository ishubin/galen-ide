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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum MockRegistry {
    INSTANCE;

    private Map<MockKey, Object> mocks = new HashMap<>();

    private <T> void register(String mockUniqueKey, T mock, String mockName) {
        MockKey mockKey = new MockKey(mockUniqueKey, mockName);
        mocks.put(mockKey, mock);
    }

    @SuppressWarnings("unchecked")
    private synchronized <T> Optional<T> pick(String mockUniqueKey, String mockName) {
        MockKey mockKey = new MockKey(mockUniqueKey, mockName);
        if (mocks.containsKey(mockKey)) {
            return Optional.ofNullable((T) mocks.get(mockKey));
        }
        return Optional.empty();
    }

    public static <T> void registerMock(String mockUniqueKey, T mock, String mockName) {
        MockRegistry.INSTANCE.register(mockUniqueKey, mock, mockName);
    }

    public static <T> Optional<T> pickMock(String mockUniqueKey, String mockName) {
        return MockRegistry.INSTANCE.pick(mockUniqueKey, mockName);
    }

}
