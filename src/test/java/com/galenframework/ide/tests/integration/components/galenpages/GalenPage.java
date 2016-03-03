package com.galenframework.ide.tests.integration.components.galenpages;

import org.openqa.selenium.WebDriver;

import java.util.List;
import static java.util.stream.Collectors.toList;

public abstract class GalenPage<T extends GalenPage> extends GalenComponent<T> {

    public GalenPage(String name, WebDriver driver) {
        super(driver, name, driver);
    }

    @SuppressWarnings("unchecked")
    public T waitForIt() {
        new Wait(getName() + " to be loaded").untilALL(availabilityElements().stream().map(WaitCheckPresent::new).collect(toList()));
        return (T) this;
    }


    public abstract List<WebComponent> availabilityElements();
}
