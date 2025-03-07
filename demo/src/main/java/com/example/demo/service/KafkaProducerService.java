package com.example.demo.service;

import com.example.demo.controller.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic}")
    private String topic;

    public CompletableFuture<Void> sendMessage(MessageDto messageDto) {
        log.info("Sending message to topic [{}]: {}", topic, messageDto.content());
        return sendMessageToKafka(messageDto.content())
                .exceptionally(ex -> handleSendFailure(messageDto.content(), ex));
    }

    private CompletableFuture<Void> sendMessageToKafka(String message) {
        return kafkaTemplate.send(topic, message).thenAccept(this::logSuccessMessage);
    }

    private void logSuccessMessage(SendResult<String, String> result) {
        log.info("Message [{}] sent successfully to partition {} with offset {}",
                result.getProducerRecord().value(),
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().offset());
    }

    private Void handleSendFailure(String message, Throwable ex) {
        logFailureMessage(topic, message, ex);
        if (ex.getCause() instanceof KafkaException) {
            throw new KafkaException("Kafka send failed: " + ex);
        }
        throw new RuntimeException("Unexpected error while sending message: ", ex);
    }

    private void logFailureMessage(String topic, String message, Throwable ex) {
        log.error("Failed to send message [{}] to topic [{}] due to: {}", message, topic, ex.getMessage());
    }
}

