package com.galenframework.ide.tests.integration;

import com.galenframework.ide.tests.integration.components.MockedWebApp;
import com.galenframework.ide.tests.integration.mocks.MockRegistry;
import com.galenframework.ide.tests.integration.mocks.MockedServiceProvider;
import com.galenframework.testng.GalenTestNgTestBase;
import org.mockito.Mockito;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.reset;

public class GalenTestBase extends GalenTestNgTestBase {

    private String mockUniqueKey = UUID.randomUUID().toString();
    private static final long ONE_YEAR = 31556952000L;

    protected <T> T registerMock(Class<T> mockClass) {
        return registerMock(Mockito.mock(mockClass), mockClass);
    }

    private List<Object> mocks = new LinkedList<>();

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
        driver.get("http://localhost:4567");
        driver.manage().deleteAllCookies();
        driver.manage().addCookie(new Cookie(MockedServiceProvider.MOCK_KEY_COOKIE_NAME, mockUniqueKey, "/", new Date(new Date().getTime() + ONE_YEAR)));
        return driver;
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
}
