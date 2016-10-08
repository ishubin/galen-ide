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
package com.galenframework.ide.tests.integration.ui;

import com.galenframework.ide.tests.integration.components.MockedWebApp;
import com.galenframework.ide.tests.integration.mocks.MockRegistry;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.testng.GalenTestNgTestBase;
import org.mockito.Mockito;

import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.*;
import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.reset;
import static spark.Spark.stop;

public class GalenTestBase extends GalenTestNgTestBase {
    private static final Dimension DESKTOP_SIZE = new Dimension(1228, 800);
    private static final long ONE_YEAR = 31556952000L;
    private Logger LOG = LoggerFactory.getLogger(getClass());

    private String mockUniqueKey = provideMockUniqueKey();

    private String provideMockUniqueKey() {
        String id = UUID.randomUUID().toString();
        LOG.info(format("Generating mock unique key %s for test %s", id, getClass().getSimpleName()));
        return id;
    }


    protected <T> T registerMockitoMock(Class<T> mockClass) {
        return registerMock(Mockito.mock(mockClass), mockClass);
    }

    private List<Object> mocks = new LinkedList<>();

    protected <T> T registerMock(T mock, Class<T> mockClass) {
        MockRegistry.registerMock(mockUniqueKey, mock, mockClass.getName());
        mocks.add(mock);
        return mock;
    }

    @BeforeMethod
    public void resetAllMocks() {
        reset(mocks.toArray(new Object[mocks.size()]));
    }

    @Override
    public WebDriver createDriver(Object[] args) {
        WebDriver driver = WebDriverSingleInstance.getDriver();
        driver.get(getTestUrl() + "/_test-start_");

        driver.manage().window().setSize(DESKTOP_SIZE);

        driver.manage().deleteAllCookies();
        LOG.info(format("Setting cookie for mock key %s", mockUniqueKey));
        driver.manage().addCookie(new Cookie(MockedWebApp.MOCK_KEY_COOKIE_NAME, mockUniqueKey, "/", new Date(new Date().getTime() + ONE_YEAR)));
        return driver;
    }

    protected String getTestUrl() {
        return "http://localhost:4567";
    }

    protected void loadDefaultTestUrl() {
        LOG.info(format("Loading url %s", getTestUrl()));
        load(getTestUrl());
    }


    @BeforeSuite
    public void startupMockedWebApp() throws IOException {
        MockedWebApp.create();
    }

    @AfterMethod
    @Override
    public void quitDriver() {
       // do nothing so that single driver stays on
    }

    @AfterSuite
    public void closeSingleDriver() {
        if (WebDriverSingleInstance.wasInstantiated()) {
            WebDriverSingleInstance.getDriver().quit();
        }
    }

    @AfterSuite
    public void closeWebServer() {
        stop();
    }

    public void checkLayout(String specPath) throws IOException {
        checkLayout(specPath, emptyList());
    }

    public void checkLayout(String specPath, List<String> tags, HashMap<String, Object> specVariables) throws IOException {
        checkLayout(specPath, new SectionFilter(tags, emptyList()), new Properties(), specVariables);
    }

}
