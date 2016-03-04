package com.galenframework.ide.tests.integration.components.pages;

import com.galenframework.ide.tests.integration.components.galenpages.GalenComponent;
import com.galenframework.ide.tests.integration.components.galenpages.WebLocatorComponent;
import org.openqa.selenium.By;

import static org.openqa.selenium.By.cssSelector;

public class CustomSizeProvider extends WebLocatorComponent<CustomSizeProvider> {
    public final WebLocatorComponent<WebLocatorComponent> sizes = textfield("Sizes", cssSelector("input[name='sizes']"));

    public CustomSizeProvider(GalenComponent parent, By locator) {
        super("Custom Size Provider", parent, locator);
    }
}
