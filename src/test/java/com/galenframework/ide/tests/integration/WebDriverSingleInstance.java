package com.galenframework.ide.tests.integration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebDriverSingleInstance {
    private static WebDriver _instance = null;

    public synchronized static WebDriver getDriver() {
        if (_instance == null) {
            _instance = new FirefoxDriver();
        }
        return _instance;
    }

    public static boolean wasInstantiated() {
        return _instance != null;
    }
}
