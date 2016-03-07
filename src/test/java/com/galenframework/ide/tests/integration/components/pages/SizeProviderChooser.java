package com.galenframework.ide.tests.integration.components.pages;

import com.galenframework.ide.tests.integration.components.galenpages.FinalElement;
import com.galenframework.ide.tests.integration.components.galenpages.GalenComponent;
import com.galenframework.ide.tests.integration.components.galenpages.WebLocatorComponent;
import org.openqa.selenium.By;

import static org.openqa.selenium.By.cssSelector;

public class SizeProviderChooser extends WebLocatorComponent<SizeProviderChooser> {
    public FinalElement customRadio = button("Custom radio", cssSelector("[type='radio'][value='custom']"));
    public FinalElement rangeRadio = button("Range radio", cssSelector("[type='radio'][value='range']"));
    public FinalElement unsupportedRadio = button("Unsupported radio", cssSelector("[type='radio'][value='unsupported']"));

    public SizeProviderChooser(GalenComponent parent, By locator) {
        super("Size Provider Chooser", parent, locator);
    }
}
