package com.galenframework.ide.tests.integration.components;


import java.awt.*;
import java.util.List;

public class TestDevice {
    private String name;
    private Dimension size;
    private List<String> tags;

    public TestDevice(String name, Dimension size, List<String> tags) {
        this.name = name;
        this.size = size;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
