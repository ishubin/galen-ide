package com.galenframework.ide;


import java.util.List;

public class CreateDeviceRequest {
    private String browserType = "";
    private String name = "";
    private List<String> tags;
    private List<Size> sizes;
    private String sizeType;
    private SizeVariation sizeVariation;

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
    }

    public SizeVariation getSizeVariation() {
        return sizeVariation;
    }

    public void setSizeVariation(SizeVariation sizeVariation) {
        this.sizeVariation = sizeVariation;
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }
}
