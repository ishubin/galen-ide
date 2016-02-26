package com.galenframework.ide.tests.integration.mocks.services;

import com.galenframework.ide.TestCommand;
import com.galenframework.ide.services.RequestData;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.services.tester.TesterService;
import com.galenframework.ide.tests.integration.mocks.MockedServiceProvider;

public class DefaultTestServiceMock implements TesterService {

    @Override
    public void runtTest(RequestData requestData, TestCommand testCommand) {

    }

    @Override
    public TestCommand getLastTestCommand(RequestData requestData) {
        return null;
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return null;
    }
}
