package com.galenframework.ide.services;

import spark.Request;

import java.util.Map;
import java.util.Optional;

public class RequestData {
    private final Map<String, String> cookies;

    public RequestData(Request req) {
        this.cookies = req.cookies();
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Optional<String> cookie(String mockKeyCookieName) {
        if (cookies != null) {
            return Optional.ofNullable(cookies.get(mockKeyCookieName));
        } else {
            return Optional.empty();
        }
    }
}
