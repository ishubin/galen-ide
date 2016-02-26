package com.galenframework.ide.tests.integration.mocks;

import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.Service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;

import static java.lang.String.format;

public class ProxyServiceInvokationHandler<T extends Service> implements InvocationHandler {

    private final Class<T> serviceClass;
    private final T defaultMock;

    public  ProxyServiceInvokationHandler(Class<T> serviceClass, T defaultMock) {
        this.serviceClass = serviceClass;
        this.defaultMock = defaultMock;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Optional<RequestData> requestData  = obtainRequestData(args);

        if (requestData.isPresent()) {
            Optional<String> cookie = requestData.get().cookie(MockedServiceProvider.MOCK_KEY_COOKIE_NAME);
            if (cookie.isPresent()) {
                String mockUniqueKey = cookie.get();
                Optional<T> mock = MockRegistry.pickMock(mockUniqueKey, serviceClass);
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
