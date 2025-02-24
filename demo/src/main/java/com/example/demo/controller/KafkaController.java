package com.example.demo.controller;

import com.example.demo.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/producer")
public class KafkaController {

    private final KafkaProducerService kafkaProducerService;

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        kafkaProducerService.sendMessage("test-topic", message);
        return "Message has been sent: " + message;
    }

    @GetMapping("/admin")
    public String adminAccess() {
        return "Kafka-Producer-Service: Access granted for admin!";
    }

    @GetMapping("/user")
    public String userAccess() {
        return "Kafka-Producer-Service: Access granted for user!";
    }

}
