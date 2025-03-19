package com.example.demo.controller;

import com.example.demo.controller.dto.MessageDto;
import com.example.demo.service.KafkaProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/producer")
public class KafkaController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/send")
    public void sendMessage(@Valid @RequestBody MessageDto messageDto) {
        kafkaProducerService.sendMessage(messageDto);
        log.info("Message sent: {}", messageDto.content());
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminAccess() {
        return ResponseEntity.ok("Kafka-Producer-Service: Access granted for admin!");
    }

    @GetMapping("/user")
    public ResponseEntity<String> userAccess() {
        return ResponseEntity.ok("Kafka-Producer-Service: Access granted for user!");
    }
}
