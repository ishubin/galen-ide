package com.galenframework.ide.tests.integration.mocks;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class MockKey {
    private final String uniqueKey;
    private final String mockClass;

    public MockKey(String uniqueKey, Class<?> mockClass) {
        this.uniqueKey = uniqueKey;
        this.mockClass = mockClass.getName();
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public String getMockClass() {
        return mockClass;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(uniqueKey)
                .append(mockClass)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } if (obj == this) {
            return true;
        } if (!(obj instanceof MockKey)) {
            return false;
        }
        MockKey rhs = (MockKey) obj;

        return new EqualsBuilder()
                .append(uniqueKey, rhs.uniqueKey)
                .append(mockClass, rhs.mockClass)
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uniqueKey", uniqueKey)
                .append("mockClass", mockClass)
                .toString();
    }
}
