package com.galenframework.ide.tests.integration.components.galenpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Constructor;
import java.util.List;

public class WebList<T extends WebCachedComponent> extends GalenComponent<WebList> {
    private final Class<T> elementsClass;
    private final By elementsLocator;
    private final GalenComponent parent;
    private List<WebElement> cachedElements;

    public WebList(String name, GalenComponent parent, Class<T> elementsClass, By elementsLocator) {
        super(parent.getDriver(), name, parent);
        this.parent = parent;
        this.elementsClass = elementsClass;
        this.elementsLocator = elementsLocator;
    }

    @Override
    public WebList<T> waitForIt() {
        return this;
    }

    public T get(int index) {
        if (cachedElements == null) {
            cachedElements = findElements(elementsLocator);
        }
        return createItemFor(index, cachedElements.get(index));
    }

    private T createItemFor(int index, WebElement webElement) {
        try {
            Constructor<T> constructor = elementsClass.getConstructor(String.class, GalenComponent.class, WebElement.class);
            return constructor.newInstance(getName() + "#" + index, parent, webElement);
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate weblist element", e);
        }
    }
}
