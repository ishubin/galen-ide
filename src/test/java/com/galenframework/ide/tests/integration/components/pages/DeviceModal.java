package com.galenframework.ide.tests.integration.components.pages;

import com.galenframework.ide.tests.integration.components.galenpages.GalenComponent;
import com.galenframework.ide.tests.integration.components.galenpages.WebLocatorComponent;
import org.openqa.selenium.By;

import static org.openqa.selenium.By.cssSelector;

public class DeviceModal extends WebLocatorComponent<DeviceModal> {
    public WebLocatorComponent<WebLocatorComponent> name = textfield("Name", cssSelector("[name='name']"));
    public WebLocatorComponent<WebLocatorComponent> browser = dropdown("Browser", cssSelector("[name='browserType']"));
    public WebLocatorComponent<WebLocatorComponent> tags = textfield("Tags", cssSelector("[name='tags']"));
    public WebLocatorComponent<WebLocatorComponent> submitButton = button("Submit", cssSelector("button.action-device-submit"));
    public CustomSizeProvider customSizeProvider = new CustomSizeProvider(this, cssSelector(".settings-form-group-size[data-type='custom']"));


    public DeviceModal(GalenComponent parent, By locator) {
        super("Device Modal", parent, locator);
    }
}
