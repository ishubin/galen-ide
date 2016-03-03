package com.galenframework.ide.tests.integration.components.pages;

import com.galenframework.ide.tests.integration.components.galenpages.WebLocatorComponent;
import com.galenframework.ide.tests.integration.components.galenpages.GalenComponent;
import org.openqa.selenium.By;

import static org.openqa.selenium.By.cssSelector;

public class Header extends WebLocatorComponent<Header> {

    public Header(GalenComponent parent, By locator) {
        super("Header", parent, locator);
    }

    public final WebLocatorComponent loadProfileLink = link("Load Profiles", cssSelector(".action-profiles-load"));

    @Override
    public Header waitForIt() {
        return waitUntilPresent();
    }
}
