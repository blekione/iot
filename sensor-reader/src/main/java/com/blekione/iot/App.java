package com.blekione.iot;

import java.io.InputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws Exception {
        InputStream config = App.class.getClassLoader().getResourceAsStream("config.json");
        System.out.println("config" + config);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(config);
        System.out.println(jsonNode.get("node1").toString());
        IOTNode iotNode = IOTNode.create(jsonNode.get("node1").toString());
        iotNode.addResource("GroveTempHumD0/humidity");
        System.out.println(iotNode.getResourceValue("GroveTempHumD0/humidity"));
        System.out.println(iotNode.getResourceURI("GroveTempHumD0/temperature"));
        System.out.println(iotNode.getResourceValue("GroveTempHumD0/temperature"));
        System.out.println(iotNode.getResourceValue("GroveLuminanceA0/luminance"));
        iotNode.getResourceNames().forEach(System.out::println);

    }
}
