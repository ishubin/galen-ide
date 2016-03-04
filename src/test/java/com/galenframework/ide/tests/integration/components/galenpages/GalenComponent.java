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
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public abstract class GalenComponent<T extends GalenComponent> implements SearchContext {
    private final WebDriver driver;
    private final String name;
    private final SearchContext searchContext;

    public GalenComponent(WebDriver driver, String name, SearchContext searchContext) {
        this.driver = driver;
        this.name = name;
        this.searchContext = searchContext;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return getSearchContext().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return getSearchContext().findElement(by);
    }


    public SearchContext getSearchContext() {
        return searchContext;
    }


    public WebDriver getDriver() {
        return driver;
    }

    public abstract T waitForIt();
}
