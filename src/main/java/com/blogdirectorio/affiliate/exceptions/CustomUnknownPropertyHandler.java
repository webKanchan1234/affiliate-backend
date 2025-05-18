package com.blogdirectorio.affiliate.exceptions;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;

import java.util.ArrayList;
import java.util.List;

public class CustomUnknownPropertyHandler extends DeserializationProblemHandler {

    private final List<String> unknownProperties = new ArrayList<>();

    @Override
    public boolean handleUnknownProperty(DeserializationContext ctxt,
                                         JsonParser p,
                                         com.fasterxml.jackson.databind.JsonDeserializer<?> deserializer,
                                         Object beanOrClass,
                                         String propertyName) {

        unknownProperties.add("Unknown key: " + propertyName);
        try {
            p.skipChildren(); // Skip the value (if it's an object or array)
        } catch (Exception ignored) {}
        return true; // handled
    }

    public List<String> getUnknownProperties() {
        return unknownProperties;
    }

    public void clear() {
        unknownProperties.clear();
    }
}
