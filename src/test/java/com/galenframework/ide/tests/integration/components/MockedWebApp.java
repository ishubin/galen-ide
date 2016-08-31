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
package com.galenframework.ide.tests.integration.components;

import com.galenframework.ide.model.settings.IdeArguments;
import com.galenframework.ide.Main;
import com.galenframework.ide.services.ServiceProvider;
import com.galenframework.ide.tests.integration.mocks.CurrentMockSession;
import com.galenframework.ide.tests.integration.mocks.MockedServiceProvider;

import java.io.IOException;

import static spark.Spark.awaitInitialization;
import static spark.Spark.before;

public class MockedWebApp extends Main {
    private static MockedWebApp _instance = null;
    public static final String MOCK_KEY_COOKIE_NAME = "__MockUniqueKey__";

    private MockedWebApp() throws IOException {
        super("target/reports");
        init();
    }

    private void init() {
        ServiceProvider serviceProvider = new MockedServiceProvider();
        IdeArguments ideArguments = new IdeArguments();
        ideArguments.setPort(4567);
        initWebServer(serviceProvider, ideArguments);
        initMockRequestFilter();
        awaitInitialization();
    }

    private void initMockRequestFilter() {
        before((request, response) -> {
            CurrentMockSession.clear();
            String mockUniqueKey = request.cookie(MOCK_KEY_COOKIE_NAME);
            if (mockUniqueKey != null) {
                CurrentMockSession.register(mockUniqueKey);
            }
        });
    }

    public synchronized static void create() throws IOException {
        if (_instance == null) {
            _instance = new MockedWebApp();
        }
    }
}
