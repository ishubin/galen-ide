package com.galenframework.ide.tests.integration.mocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum MockRegistry {
    INSTANCE;

    private Map<MockKey, Object> mocks = new HashMap<>();

    private <T> void register(String mockUniqueKey, T mock, Class<T> mockClass) {
        MockKey mockKey = new MockKey(mockUniqueKey, mockClass);
        mocks.put(mockKey, mock);
    }

    private synchronized <T> Optional<T> pick(String mockUniqueKey, Class<T> mockClass) {
        MockKey mockKey = new MockKey(mockUniqueKey, mockClass);
        if (mocks.containsKey(mockKey)) {
            return Optional.ofNullable((T) mocks.get(mockKey));
        }
        return Optional.empty();
    }

    public static <T> void registerMock(String mockUniqueKey, T mock, Class<T> mockClass) {
        MockRegistry.INSTANCE.register(mockUniqueKey, mock, mockClass);
    }

    public static <T> Optional<T> pickMock(String mockUniqueKey, Class<T> mockClass) {
        return MockRegistry.INSTANCE.pick(mockUniqueKey, mockClass);
    }

}
