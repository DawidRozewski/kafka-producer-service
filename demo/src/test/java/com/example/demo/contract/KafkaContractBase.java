package com.example.demo.contract;

import com.example.demo.controller.dto.MessageDto;
import com.example.demo.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;

@SpringBootTest
@AutoConfigureMessageVerifier
public abstract class KafkaContractBase {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public void sendMessageTriggered() {
        kafkaProducerService.sendMessage(new MessageDto("Hello Kafka!"));
    }
}
