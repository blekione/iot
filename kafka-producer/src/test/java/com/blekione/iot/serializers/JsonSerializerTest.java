package com.blekione.iot.serializers;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.blekione.iot.records.DataRecord;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSerializerTest {

    @Test
    public void test() throws IOException {
        final DataRecord record = new DataRecord("test", 10);
        final Map<String, String> conf = new HashMap<>();
        conf.put("serializer.jackson.cbor", "true");
        final JsonSerializer serializer = new JsonSerializer(new ObjectMapper());
        serializer.configure(conf, true);
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(record));
        byte[] result = serializer.serialize("test_topic", record);
        System.out.println(Arrays.toString(result));
        serializer.close();


    }

}
