package com.galenframework.ide.controllers.actions;

import java.util.List;

public class DeviceActionCheckLayoutRequest {

    private String path;
    private List<String> tags;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
