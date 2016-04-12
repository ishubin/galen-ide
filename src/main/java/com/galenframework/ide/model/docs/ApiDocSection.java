package com.galenframework.ide.model.docs;

import java.util.List;

public class ApiDocSection {
    private final String section;
    private final List<Object> requests;

    public ApiDocSection(String section, List<Object> requests) {
        this.section = section;
        this.requests = requests;
    }

    public String getSection() {
        return section;
    }

    public List<Object> getRequests() {
        return requests;
    }
}
