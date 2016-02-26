package com.galenframework.ide.tests.integration.mocks;

import com.galenframework.ide.services.Service;

import java.lang.reflect.Proxy;

public class CookieBasedMockedService<T extends Service> {
    private final Class<T> serviceClass;
    private final T defaultMock;

    private T service;

    public CookieBasedMockedService(Class<T> serviceClass, T defaultMock) {
        this.serviceClass = serviceClass;
        this.service = createProxyService();
        this.defaultMock = defaultMock;
    }

    private T createProxyService() {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class<?>[] {serviceClass},
                new ProxyServiceInvokationHandler(serviceClass, defaultMock)
        );
    }

    public T getService() {
        return service;
    }
}
