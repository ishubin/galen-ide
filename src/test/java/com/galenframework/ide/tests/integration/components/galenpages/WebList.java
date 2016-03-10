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
import org.openqa.selenium.WebElement;

import java.lang.reflect.Constructor;

public class WebList<T extends WebCachedComponent> extends GalenComponent<WebList> {
    private final Class<T> elementsClass;
    private final By elementsLocator;
    private final GalenComponent parent;

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
        return createItemFor(index, findElements(elementsLocator).get(index));
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
