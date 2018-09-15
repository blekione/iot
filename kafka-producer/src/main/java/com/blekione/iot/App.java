package com.blekione.iot;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        final Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.0.57:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        final KafkaProducer<Object, Object> producer = new KafkaProducer<>(props);
        producer.
    }
}
