package com.galenframework.ide.tests.integration.components.pages;

import com.galenframework.ide.tests.integration.components.galenpages.GalenComponent;
import com.galenframework.ide.tests.integration.components.galenpages.WebLocatorComponent;
import org.openqa.selenium.By;

public class FileBrowserPanel extends WebLocatorComponent<FileBrowserPanel> {
    public FileBrowserPanel(GalenComponent parent, By locator) {
        super("File browser Panel", parent, locator);
    }
}
