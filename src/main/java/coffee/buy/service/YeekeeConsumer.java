package coffee.buy.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import coffee.buy.vo.req.SubmitYeeKeeReq;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class YeekeeConsumer {

    @Autowired
    private BuyYeekeeService buyYeekeeService;

    @KafkaListener(topics = "coffee-yeekee")
    public void listen(ConsumerRecord<String, SubmitYeeKeeReq> record) {
        log.info(record.toString());
        try {
            SubmitYeeKeeReq req = record.value();
            System.out.println(req);
            // buyYeekeeService.submitValueSum(req);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}