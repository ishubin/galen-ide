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

import com.galenframework.ide.tests.integration.components.galenpages.GalenComponent;
import com.galenframework.ide.tests.integration.components.galenpages.WebComponent;
import com.galenframework.ide.tests.integration.components.galenpages.WebList;
import com.galenframework.ide.tests.integration.components.galenpages.WebLocatorComponent;
import org.openqa.selenium.By;

import static org.openqa.selenium.By.cssSelector;


public class DevicePanel extends WebLocatorComponent<DevicePanel> {

    public DevicePanel(GalenComponent parent, By locator) {
        super("Devices Panel", parent, locator);
    }

    public final WebComponent addNewDeviceLink = link("Add New Device", cssSelector(".action-devices-add-new"));

    public final WebList<DeviceRow> devices = new WebList<>("Profiles", this, DeviceRow.class, cssSelector("table tbody tr"));
}
