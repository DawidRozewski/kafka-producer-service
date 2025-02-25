package com.example.demo.controller;

import com.example.demo.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/producer")
public class KafkaController {

    private final KafkaProducerService kafkaProducerService;

    @GetMapping("/send")
    public void sendMessage(@RequestParam String message) {
        kafkaProducerService.sendMessage(message);
        log.info("Message has been sent: {}", message);
    }

    @GetMapping("/admin")
    public void adminAccess() {
        log.info("Kafka-Producer-Service: Access granted for admin!");
    }

    @GetMapping("/user")
    public void userAccess() {
        log.info("Kafka-Producer-Service: Access granted for user!");
    }

}
