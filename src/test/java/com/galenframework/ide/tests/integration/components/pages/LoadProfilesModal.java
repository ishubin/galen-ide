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
package com.galenframework.ide.tests.integration.components.pages;

import com.galenframework.ide.tests.integration.components.galenpages.*;
import org.openqa.selenium.By;

import static org.openqa.selenium.By.cssSelector;

public class LoadProfilesModal extends WebLocatorComponent<LoadProfilesModal> {
    public LoadProfilesModal(GalenComponent component, By locator) {
        super("Load Profiles modal", component, locator);
    }

    public WebList<WebCachedComponent> profiles = new WebList<>("Profiles", this, WebCachedComponent.class, cssSelector("a.profile-file-item"));

    @Override
    public LoadProfilesModal waitForIt() {
        return waitUntilPresent();
    }
}
