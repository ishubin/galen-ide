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

import com.galenframework.ide.tests.integration.components.galenpages.GalenPage;
import com.galenframework.ide.tests.integration.components.galenpages.WebComponent;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static java.util.Arrays.asList;
import static org.openqa.selenium.By.cssSelector;

public class IdePage extends GalenPage<IdePage> {


    public IdePage(WebDriver driver) {
        super("IDE Page", driver);
    }

    public final Header header = new Header(this, cssSelector("nav.navbar"));
    public final LoadProfilesModal loadProfilesModal = new LoadProfilesModal(this, cssSelector("#global-modal .modal .modal-dialog"));
    public final FileBrowserPanel fileBrowserPanel = new FileBrowserPanel(this, cssSelector("#file-browser"));
    public final DevicePanel devicesPanel = new DevicePanel(this, cssSelector("#devices-panel"));

    public final DeviceModal deviceModal = new DeviceModal(this, cssSelector(".modal-dialog[data-modal='device-modal']"));
    public final TestResultPanel testResultPanel = new TestResultPanel(this, cssSelector("#test-results"));

    @Override
    public List<WebComponent> availabilityElements() {
        return asList(header, fileBrowserPanel, devicesPanel, testResultPanel);
    }
}
