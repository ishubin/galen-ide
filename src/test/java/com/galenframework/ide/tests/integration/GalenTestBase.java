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
package com.galenframework.ide.tests.integration;

import com.galenframework.browser.SeleniumBrowser;
import com.galenframework.ide.tests.integration.components.DeviceRunner;
import com.galenframework.ide.tests.integration.components.MockedWebApp;
import com.galenframework.ide.tests.integration.components.TestDevice;
import com.galenframework.ide.tests.integration.mocks.MockRegistry;
import com.galenframework.ide.tests.integration.mocks.MockedServiceProvider;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.suite.actions.GalenPageActionRunJavascript;
import com.galenframework.testng.GalenTestNgTestBase;
import org.mockito.Mockito;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.reset;
import static spark.Spark.stop;

public class GalenTestBase extends GalenTestNgTestBase {

    private String mockUniqueKey = UUID.randomUUID().toString();
    private static final long ONE_YEAR = 31556952000L;

    protected <T> T registerMock(Class<T> mockClass) {
        return registerMock(Mockito.mock(mockClass), mockClass);
    }

    private List<Object> mocks = new LinkedList<>();

    private  TestDevice desktopDevice = new TestDevice("Desktop", new Dimension(1224, 800), singletonList("desktop"));
    private TestDevice tabletDevice = new TestDevice("Tablet", new Dimension(800, 800), singletonList("tablet"));
    private List<TestDevice> allTestDevices = asList(desktopDevice, tabletDevice);

    @DataProvider
    public Object[][] allDevices() {
        return new Object[][] {
                {desktopDevice},
                {tabletDevice}
        };
    }

    protected <T> T registerMock(T mock, Class<T> mockClass) {
        MockRegistry.registerMock(mockUniqueKey, mock, mockClass);
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
        driver.get(getTestUrl());
        driver.manage().deleteAllCookies();
        driver.manage().addCookie(new Cookie(MockedServiceProvider.MOCK_KEY_COOKIE_NAME, mockUniqueKey, "/", new Date(new Date().getTime() + ONE_YEAR)));
        return driver;
    }

    protected String getTestUrl() {
        return "http://localhost:4567";
    }

    protected void loadDefaultTestUrl() {
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

    public void onDesktopDevice(DeviceRunner deviceRunner) {
        runOnDevice(desktopDevice, deviceRunner);
    }

    private void runOnDevice(TestDevice testDevice, DeviceRunner deviceRunner) {
        getReport().sectionStart("Testing on device " + testDevice.getName());
        loadDefaultTestUrl();
        try {
            resizeFor(testDevice);
            Thread.sleep(500);
            deviceRunner.run(testDevice);
        } catch (Exception ex) {
            throw new RuntimeException("Exception in device " + testDevice.getName(), ex);
        } finally {
            getReport().sectionEnd();
        }
    }

    public void onEveryDevice(DeviceRunner deviceRunner) {
        for (TestDevice testDevice : allTestDevices) {
            runOnDevice(testDevice, deviceRunner);
        }
    }

    public void resizeFor(TestDevice testDevice) {
        resize(testDevice.getSize().width, testDevice.getSize().height);
    }
}
