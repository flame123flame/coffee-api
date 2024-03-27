package coffee.buy.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import coffee.buy.vo.req.SubmitYeeKeeReq;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import lombok.extern.slf4j.Slf4j;

@Component
public class RunCustomerYeeKee {

    @Autowired
    private Environment environment;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SubmitYeeKeeReq> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SubmitYeeKeeReq> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, SubmitYeeKeeReq> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "coffee");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // props.put(ConsumerConfig.GROUP_ID_CONFIG, "coffee");
        // props.put("enable.auto.commit", "true");
        // props.put("auto.commit.interval.ms", "1000");
        // props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        // StringDeserializer.class);
        // props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        // JsonDeserializer.class);
        // props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.demo.customer");
        return props;
    }
}