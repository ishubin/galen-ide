package com.galenframework.ide.services;

import spark.Request;

import java.util.Map;

public class RequestData {
    private final Map<String, String> cookies;

    public RequestData(Request req) {
        this.cookies = req.cookies();
    }

    public Map<String, String> getCookies() {
        return cookies;
    }
}
