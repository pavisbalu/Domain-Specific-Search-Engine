package edu.bits.wilp.ir_assignment.utils;

import kong.unirest.JsonObjectMapper;

public class JsonSerDe {
    private static final JsonObjectMapper mapper = new JsonObjectMapper();

    public static String toJson(Object value) {
        return mapper.writeValue(value);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return mapper.readValue(json, type);
    }
}
