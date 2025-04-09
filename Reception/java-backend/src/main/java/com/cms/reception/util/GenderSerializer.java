package com.cms.reception.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class GenderSerializer extends JsonSerializer<Integer> {

    @Override
    public void serialize(Integer value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        
        switch (value) {
            case 0:
                gen.writeString("male");
                break;
            case 1:
                gen.writeString("female");
                break;
            case 2:
                gen.writeString("other");
                break;
            default:
                gen.writeString(value.toString());
        }
    }
} 