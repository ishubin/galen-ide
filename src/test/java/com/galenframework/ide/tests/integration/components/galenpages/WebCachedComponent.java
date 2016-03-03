package com.galenframework.ide.tests.integration.components.galenpages;

import org.openqa.selenium.WebElement;

public class WebCachedComponent<T extends GalenComponent> extends WebComponent<T> {

    private final WebElement cachedElement;

    public WebCachedComponent(String name, GalenComponent parent, WebElement cachedElement) {
        super(name, parent);
        this.cachedElement = cachedElement;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T waitForIt() {
        new Wait("Until " + getName() + " is present").until(this::isPresent);
        return (T) this;
    }

    @Override
    protected WebElement getWebElement() {
        return cachedElement;
    }
}
