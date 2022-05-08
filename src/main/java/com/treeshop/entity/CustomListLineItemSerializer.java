package com.treeshop.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomListLineItemSerializer extends StdSerializer<List<LineItemEntity>> {

    public CustomListLineItemSerializer() {
        this(null);
    }

    public CustomListLineItemSerializer(Class<List<LineItemEntity>> t) {
        super(t);
    }
    @Override
    public void serialize(
            List<LineItemEntity> items,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException {

        List<LineItemEntity> ids = new ArrayList<>();
        for (LineItemEntity item : items) {
            ids.add(item);
        }
        generator.writeObject(ids);
    }
}
