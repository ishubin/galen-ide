package com.galenframework.ide.tests.integration.components;

import com.galenframework.ide.Main;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.tests.integration.mocks.MockedServiceProvider;

import java.io.IOException;

public class MockedWebApp extends Main {
    private static MockedWebApp _instance = null;


    private MockedWebApp() throws IOException {
        super();
        init();
    }

    private void init() {
        ServiceProvider serviceProvider = new MockedServiceProvider();
        initWebServer(serviceProvider);
    }

    public synchronized static void create() throws IOException {
        if (_instance == null) {
            _instance = new MockedWebApp();
        }
    }
}
