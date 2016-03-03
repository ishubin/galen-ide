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

    @Override
    public List<WebComponent> availabilityElements() {
        return asList(header);
    }
}
