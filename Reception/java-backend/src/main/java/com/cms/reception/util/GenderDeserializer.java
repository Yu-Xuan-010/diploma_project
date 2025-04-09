package com.cms.reception.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class GenderDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        if (value == null) {
            return null;
        }
        
        switch (value.toLowerCase()) {
            case "male":
                return 0;
            case "female":
                return 1;
            case "other":
                return 2;
            default:
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    throw new IOException("无效的性别值，只能是 male、female、other 或数字", e);
                }
        }
    }
} 