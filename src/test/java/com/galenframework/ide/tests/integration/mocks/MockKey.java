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
