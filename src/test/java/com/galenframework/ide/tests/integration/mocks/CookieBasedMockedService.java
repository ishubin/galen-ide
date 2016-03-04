package com.galenframework.ide.tests.integration.mocks;

import com.galenframework.ide.services.Service;

import java.lang.reflect.Proxy;
import java.util.Optional;

public class CookieBasedMockedService<T extends Service> {
    private final Class<T> serviceClass;
    private final T defaultMock;

    private Optional<T> service = Optional.empty();

    public CookieBasedMockedService(Class<T> serviceClass, T defaultMock) {
        this.serviceClass = serviceClass;
        this.defaultMock = defaultMock;
    }

    @SuppressWarnings("unchecked")
    private T createProxyService() {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class<?>[] {serviceClass},
                new ProxyServiceInvokationHandler(serviceClass, defaultMock)
        );
    }

    public T getService() {
        if (!service.isPresent()) {
            service = Optional.of(createProxyService());
        }
        return service.get();
    }
}
