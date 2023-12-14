package com.cyfrania.logback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class Json {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String, Object> readAsMap(String text) {
        try {
            return mapper.readValue(text, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
