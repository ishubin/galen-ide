package com.galenframework.ide.tests.integration.components.pages;

import com.galenframework.ide.tests.integration.components.galenpages.FinalElement;
import com.galenframework.ide.tests.integration.components.galenpages.GalenComponent;
import com.galenframework.ide.tests.integration.components.galenpages.WebCachedComponent;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.cssSelector;

public class DeviceRow extends WebCachedComponent<DeviceRow> {
    public FinalElement editButton = button("Edit Device", cssSelector("button.action-edit-device"));
    public FinalElement deleteButton = button("Delete Device", cssSelector("button.action-delete-device"));

    public DeviceRow(String name, GalenComponent parent, WebElement cachedElement) {
        super(name, parent, cachedElement);
    }
}
