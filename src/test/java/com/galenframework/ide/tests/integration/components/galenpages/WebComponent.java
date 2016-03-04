package com.galenframework.ide.tests.integration.components.galenpages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;

public abstract class WebComponent<T extends GalenComponent> extends GalenComponent<T> {

    private final GalenComponent parent;

    public WebComponent(String name, GalenComponent parent) {
        super(parent.getDriver(), name + " on " + parent.getName(), parent);
        this.parent = parent;
    }

    protected WebLocatorComponent<WebLocatorComponent> link(String name, By locator) {
        return new WebLocatorComponent<>(name + " link", this, locator);
    }

    protected WebLocatorComponent<WebLocatorComponent> textfield(String name, By locator) {
        return new WebLocatorComponent<>(name + " textfield", this, locator);
    }

    protected WebLocatorComponent<WebLocatorComponent> dropdown(String name, By locator) {
        return new WebLocatorComponent<>(name + " dropdown", this, locator);
    }

    protected WebLocatorComponent<WebLocatorComponent> button(String name, By locator) {
        return new WebLocatorComponent<>(name + " button", this, locator);
    }


    public void click() {
        getWebElement().click();
    }

    protected abstract WebElement getWebElement();

    @Override
    public List<WebElement> findElements(By by) {
        return getWebElement().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return getWebElement().findElement(by);
    }


    @SuppressWarnings("unchecked")
    public T waitUntilPresent() {
        new Wait(getName() + " to be visible").until(this::isPresent);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T waitUntilHidden() {
        new Wait(getName() + " to be hidden").until(() -> !this.isPresent() || !this.isVisible());
        return (T) this;
    }

    public boolean isPresent() {
        try {
            getWebElement();
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    public boolean isVisible() {
        return getWebElement().isDisplayed();
    }

    public GalenComponent getParent() {
        return parent;
    }

    @SuppressWarnings("unchecked")
    public T typeText(String text) {
        getWebElement().sendKeys(text);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T selectByText(String text) {
        WebElement webElement = getWebElement();

        try {
            WebElement optionElement = webElement.findElement(By.xpath(".//option[normalize-space(.)=\"" + text + "\"]"));
            optionElement.click();
        } catch (NoSuchElementException ex) {
            throw new RuntimeException("Couldn't find option with text \"" + text + "\" for " + getName(), ex);
        }

        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T clear() {
        getWebElement().clear();
        return (T) this;
    }
}
