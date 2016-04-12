package com.galenframework.ide.model.docs;


import java.util.List;

public class ApiDocRequest {
    private final String method;
    private final String path;
    private final String description;
    private final List<Object> requestExamples;
    private final List<Object> responseExamples;
    private final String title;

    public ApiDocRequest(String method, String path, String title, String description, List<Object> requestExamples, List<Object> responseExamples) {
        this.method = method;
        this.path = path;
        this.title = title;
        this.description = description;
        this.requestExamples = requestExamples;
        this.responseExamples = responseExamples;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }

    public List<Object> getRequestExamples() {
        return requestExamples;
    }

    public List<Object> getResponseExamples() {
        return responseExamples;
    }

    public String getTitle() {
        return title;
    }
}
