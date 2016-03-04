package com.galenframework.ide.tests.integration.components.pages;

import com.galenframework.ide.tests.integration.components.galenpages.GalenComponent;
import com.galenframework.ide.tests.integration.components.galenpages.WebLocatorComponent;
import org.openqa.selenium.By;

public class DeviceModal extends WebLocatorComponent<DeviceModal> {
    public DeviceModal(GalenComponent parent, By locator) {
        super("Device Modal", parent, locator);
    }
}
