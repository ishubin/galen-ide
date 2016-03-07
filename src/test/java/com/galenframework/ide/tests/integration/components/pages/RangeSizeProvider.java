package com.galenframework.ide.tests.integration.components.pages;

import com.galenframework.ide.tests.integration.components.galenpages.GalenComponent;
import com.galenframework.ide.tests.integration.components.galenpages.WebLocatorComponent;
import org.openqa.selenium.By;

public class RangeSizeProvider extends WebLocatorComponent<RangeSizeProvider> {
    public RangeSizeProvider(GalenComponent parent, By locator) {
        super("Range Size Provider", parent, locator);
    }
}
