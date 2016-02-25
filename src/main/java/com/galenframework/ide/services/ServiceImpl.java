package com.galenframework.ide.services;

public abstract class ServiceImpl implements  Service {
    private ServiceProvider serviceProvider;

    public ServiceImpl(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }
}
