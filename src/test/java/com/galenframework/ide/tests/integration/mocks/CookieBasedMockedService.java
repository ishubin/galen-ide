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

import java.lang.reflect.Proxy;
import java.util.Optional;

public class CookieBasedMockedService<T extends Service> {
    private final Class<T> serviceClass;
    private final T defaultMock;
    private final String serviceName;

    private Optional<T> service = Optional.empty();

    public CookieBasedMockedService(String serviceName, Class<T> serviceClass, T defaultMock) {
        this.serviceName = serviceName;
        this.serviceClass = serviceClass;
        this.defaultMock = defaultMock;
    }

    public CookieBasedMockedService(Class<T> serviceClass, T defaultMock) {
        this.serviceName = serviceClass.getName();
        this.serviceClass = serviceClass;
        this.defaultMock = defaultMock;
    }

    @SuppressWarnings("unchecked")
    private T createProxyService() {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class<?>[] {serviceClass},
                new CookieBasedProxyServiceInvocationHandler(serviceName, defaultMock)
        );
    }

    public T getService() {
        if (!service.isPresent()) {
            service = Optional.of(createProxyService());
        }
        return service.get();
    }
}
