package com.galenframework.ide.tests.integration.components.galenpages;

public class WaitCheckPresent extends WaitCheck {
    private final WebComponent webComponent;

    public WaitCheckPresent(WebComponent webComponent) {
        super("to be present: " + webComponent.getName());
        this.webComponent = webComponent;
    }

    @Override
    public boolean check() {
        return webComponent.isPresent();
    }
}
