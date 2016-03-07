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

    protected FinalElement link(String name, By locator) {
        return new FinalElement(name + " link", this, locator);
    }

    protected FinalElement textfield(String name, By locator) {
        return new FinalElement(name + " textfield", this, locator);
    }

    protected FinalElement dropdown(String name, By locator) {
        return new FinalElement(name + " dropdown", this, locator);
    }

    protected FinalElement button(String name, By locator) {
        return new FinalElement(name + " button", this, locator);
    }

    protected FinalElement label(String name, By locator) {
        return new FinalElement(name + " label", this, locator);
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
