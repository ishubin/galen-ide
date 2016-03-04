package com.galenframework.ide.tests.integration.components.pages;

import com.galenframework.ide.tests.integration.components.galenpages.GalenComponent;
import com.galenframework.ide.tests.integration.components.galenpages.WebComponent;
import com.galenframework.ide.tests.integration.components.galenpages.WebLocatorComponent;
import org.openqa.selenium.By;

import static org.openqa.selenium.By.cssSelector;


public class DevicePanel extends WebLocatorComponent<DevicePanel> {

    public DevicePanel(GalenComponent parent, By locator) {
        super("Devices Panel", parent, locator);
    }

    public final WebComponent addNewDeviceLink = link("Add New Device", cssSelector(".action-devices-add-new"));
}
