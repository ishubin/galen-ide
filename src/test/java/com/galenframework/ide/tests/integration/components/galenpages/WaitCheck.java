package com.galenframework.ide.tests.integration.components.galenpages;

import java.util.List;

public abstract class WaitCheck implements WaitCheckCondition {
    private String name;

    public WaitCheck(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static WaitCheck check(String name, final WaitCheckCondition waitCheckCondition) {
        return new WaitCheck(name) {
            @Override
            public boolean check() {
                return waitCheckCondition.check();
            }
        };
    }
}
