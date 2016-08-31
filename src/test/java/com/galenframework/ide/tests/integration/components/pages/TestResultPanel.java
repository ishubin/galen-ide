package com.galenframework.ide.tests.integration.components.pages;

import com.galenframework.ide.tests.integration.components.galenpages.GalenComponent;
import com.galenframework.ide.tests.integration.components.galenpages.WebLocatorComponent;
import org.openqa.selenium.By;

public class TestResultPanel extends WebLocatorComponent<TestResultPanel> {
    public TestResultPanel(GalenComponent parent, By locator) {
        super("Test results Panel", parent, locator);
    }
}
