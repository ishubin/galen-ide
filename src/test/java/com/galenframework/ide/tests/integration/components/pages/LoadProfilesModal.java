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
