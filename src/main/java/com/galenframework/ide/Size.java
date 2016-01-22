package com.galenframework.ide;

import org.openqa.selenium.Dimension;

public class Size {
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Dimension toSeleniumDimension() {
        return new Dimension(width, height);
    }
}
