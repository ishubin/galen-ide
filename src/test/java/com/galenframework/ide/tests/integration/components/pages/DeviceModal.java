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

import com.galenframework.ide.tests.integration.components.galenpages.FinalElement;
import com.galenframework.ide.tests.integration.components.galenpages.GalenComponent;
import com.galenframework.ide.tests.integration.components.galenpages.WebLocatorComponent;
import org.openqa.selenium.By;

import static org.openqa.selenium.By.cssSelector;

public class DeviceModal extends WebLocatorComponent<DeviceModal> {
    public final FinalElement name = textfield("Name", cssSelector("[name='name']"));
    public final FinalElement browser = dropdown("Browser", cssSelector("[name='browserType']"));
    public final FinalElement tags = textfield("Tags", cssSelector("[name='tags']"));
    public final FinalElement submitButton = button("Submit", cssSelector("button.action-device-submit"));
    public final SizeProviderChooser sizeProviderChooser = new SizeProviderChooser(this, cssSelector(".btn-group.device-size-type"));
    public final CustomSizeProvider customSizeProvider = new CustomSizeProvider(this, cssSelector(".settings-form-group-size[data-type='custom']"));
    public final RangeSizeProvider rangeSizeProvider = new RangeSizeProvider(this, cssSelector(".settings-form-group-size[data-type='range']"));
    public final FinalElement unsupportedSizeProvider = label("Unsupported Size Provider", cssSelector(".settings-form-group-size[data-type='unsupported']"));


    public DeviceModal(GalenComponent parent, By locator) {
        super("Device Modal", parent, locator);
    }

    public void chooseSizeProvider(String sizeProviderType) {
        if (sizeProviderType.equals("custom")) {
            sizeProviderChooser.customRadio.click();
            customSizeProvider.waitForIt();
        } else if (sizeProviderType.equals("range")) {
            sizeProviderChooser.rangeRadio.click();
            rangeSizeProvider.waitForIt();
        } else if (sizeProviderType.equals("unsupported")) {
            sizeProviderChooser.unsupportedRadio.click();
            unsupportedSizeProvider.waitForIt();
        } else {
            throw new IllegalArgumentException("Don't know size provider type: " + sizeProviderType);
        }
    }
}
