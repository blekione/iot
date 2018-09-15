package com.blekione.iot.serializers;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.blekione.iot.records.DataRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;

public class JsonSerializer implements Closeable, AutoCloseable, Serializer<DataRecord>{

    private ObjectMapper mapper;

    public JsonSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        if (isKey && configs.containsKey("serializer.jackson.cbor")) {
            mapper = new ObjectMapper(new CBORFactory());
        }

    }

    @Override
    public byte[] serialize(String topic, DataRecord data) {
        try {
            return mapper.writeValueAsBytes(data);
        }
        catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void close() {
        mapper = null;
    }


}
