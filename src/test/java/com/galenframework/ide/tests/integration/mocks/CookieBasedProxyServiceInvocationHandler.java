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

import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.Service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;

class CookieBasedProxyServiceInvocationHandler<T extends Service> implements InvocationHandler {

    private final T defaultMock;
    private final String serviceName;

    CookieBasedProxyServiceInvocationHandler(String serviceName, T defaultMock) {
        this.serviceName = serviceName;
        this.defaultMock = defaultMock;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Optional<RequestData> requestData  = obtainRequestData(args);

        if (requestData.isPresent()) {
            Optional<String> cookie = requestData.get().cookie(MockedServiceProvider.MOCK_KEY_COOKIE_NAME);
            if (cookie.isPresent()) {
                String mockUniqueKey = cookie.get();
                Optional<T> mock = MockRegistry.pickMock(mockUniqueKey, serviceName);
                if (mock.isPresent()) {
                    return method.invoke(mock.get(), args);
                }
            }
        }
        return method.invoke(defaultMock, args);
    }

    private Optional<RequestData> obtainRequestData(Object[] args) {
        if (args != null) {
            for (Object arg : args) {
                if (arg != null && arg instanceof RequestData) {
                    return Optional.of((RequestData)arg);
                }
            }
        }
        return Optional.empty();
    }
}
