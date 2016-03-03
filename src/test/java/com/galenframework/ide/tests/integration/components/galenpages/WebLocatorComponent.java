package com.galenframework.ide.tests.integration.components.galenpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WebLocatorComponent<T extends WebLocatorComponent> extends WebComponent<T> {
    private final By locator;

    public WebLocatorComponent(String name, GalenComponent parent, By locator) {
        super(name, parent);
        this.locator = locator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T waitForIt() {
        new Wait("Until " + getName() + " is present").until(this::isPresent);
        return (T) this;
    }

    @Override
    protected WebElement getWebElement() {
        return getSearchContext().findElement(locator);
    }
}
