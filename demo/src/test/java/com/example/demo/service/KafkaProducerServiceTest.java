package com.example.demo.service;

import com.example.demo.controller.dto.MessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaProducerServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private SendResult<String, String> mockResult;

    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    private MessageDto testMessage;

    @BeforeEach
    void setUp() {
        testMessage = new MessageDto("Test message");
    }

    @Test
    void shouldSendMessageSuccessfully() {
        // Given
        CompletableFuture<SendResult<String, String>> future = CompletableFuture.completedFuture(mockResult);
        when(kafkaTemplate.send(any(), any())).thenReturn(future);

        // When & Then
        assertDoesNotThrow(() -> kafkaProducerService.sendMessage(testMessage));
        verify(kafkaTemplate, times(1)).send(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenKafkaFails() {
        // Given
        CompletableFuture<SendResult<String, String>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new KafkaException(""));

        when(kafkaTemplate.send(any(), any())).thenReturn(failedFuture);

        // When & Then
        assertThatThrownBy(() -> kafkaProducerService.sendMessage(testMessage).join())
                .hasCauseInstanceOf(KafkaException.class)
                .hasMessageContaining("Kafka send failed: ");

        verify(kafkaTemplate, times(1)).send(any(), any());
    }

    @Test
    void shouldThrowRuntimeExceptionForNonKafkaError() {
        // Given
        CompletableFuture<SendResult<String, String>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new IllegalArgumentException());

        when(kafkaTemplate.send(any(), any())).thenReturn(failedFuture);

        // When & Then
        assertThatThrownBy(() -> kafkaProducerService.sendMessage(testMessage).join())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Unexpected error while sending message");

        verify(kafkaTemplate, times(1)).send(any(), any());
    }


}