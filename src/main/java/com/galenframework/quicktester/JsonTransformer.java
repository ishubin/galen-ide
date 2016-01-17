package com.galenframework.quicktester;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String render(Object model) throws Exception {
        return objectMapper.writeValueAsString(model);
    }

    private final static JsonTransformer _instance = new JsonTransformer();

    public static JsonTransformer toJson() {
        return _instance;
    }
}
