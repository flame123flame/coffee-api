package coffee.buy.service;

import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import coffee.buy.vo.req.SubmitYeeKeeReq;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class YeeKeeSteamsCalculation {

    @Value("${common-path.kafka-url}")
    private String serverKafka;

    void putInput(SubmitYeeKeeReq req) {
        String inputTopic = "coffee-yeekee";
        Properties properties = new Properties();
        properties.put("bootstrap.servers", serverKafka);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);

        ObjectMapper objMapper = new ObjectMapper();
        try {
            String objStr = objMapper.writeValueAsString(req);
            kafkaProducer.send(new ProducerRecord(inputTopic, "1", objStr));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        kafkaProducer.close();
    }

}