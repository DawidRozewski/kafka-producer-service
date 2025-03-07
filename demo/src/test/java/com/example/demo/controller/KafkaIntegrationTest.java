package com.example.demo.controller;

import com.example.demo.AbstractConfiguredTest;
import com.example.demo.controller.dto.MessageDto;
import com.example.demo.service.KafkaProducerService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EmbeddedKafka(partitions = 1, topics = {"test-topic"})
class KafkaIntegrationTest extends AbstractConfiguredTest {

    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

    private Consumer<String, String> consumer;

    @BeforeEach
    void setUp() {
        consumer = consumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList("test-topic"));
    }

    @AfterEach
    void teardown() {
        consumer.close();
    }

    @Test
    void shouldSendAndReceiveMessage() throws Exception {
        // Given
        String testMessage = "Hello Kafka!";
        MessageDto messageDto = new MessageDto(testMessage);

        // When
        mockMvc.perform(post("/producer/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDto)));

        // Then
        ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(10));
        assertThat(records.count()).isGreaterThan(0);
    }

    @Test
    void shouldReturn500ForEmptyMessage() throws Exception {
        // Given
        String emptyMessage = "";

        // When & Then
        mockMvc.perform(post("/producer/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"" + emptyMessage + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed: Message content cannot be empty."));
    }
}
