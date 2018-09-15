package com.blekione.iot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 *
 */
public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    private static IOTNode iotNode;

    public static void main( String[] args ) throws IOException
    {
        InputStream config = App.class.getClassLoader().getResourceAsStream("config.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(config);
        iotNode = IOTNode.create(jsonNode.get("node1").toString());
        final App app = new App();
        LOG.info("value {}", app.get());
        app.send();
    }

    public void send() {
        final Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.0.57:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        final ProducerRecord<String, String> record =
            new ProducerRecord<String, String>(
                "Customeros", "Precision Products", "Englandoooo");
        try (final KafkaProducer<String, String> producer = new KafkaProducer<>(props)){
            producer.send(record, new DemoProducerCallback());
            System.out.println("sent");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double get() {
        System.out.println(iotNode.getSerialNumber());
        try {
            return iotNode.getResourceValue("GroveTempHumD0/temperature");
        }
        catch (URISyntaxException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0.0;

    }

    private class DemoProducerCallback implements Callback {

        @Override
        public void onCompletion(RecordMetadata metadata,
            Exception exception) {

            if (exception != null) {
                exception.printStackTrace();
            }
            else {
                LOG.info("successfully sent");
            }
        }

    }
}
