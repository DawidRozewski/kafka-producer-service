package com.example.demo.controller;

import com.example.demo.AbstractConfiguredTest;
import com.example.demo.service.KafkaProducerService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


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
    void shouldReturnOkWhenMessageIsSentSuccessfully() throws Exception {
        //Given
        String testMessage = "Hello from test!";

        //When
        mockMvc.perform(get("/producer/send")
                        .param("message", testMessage))
                .andExpect(status().isOk());

        //Then
        ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        assertThat(records.count()).isGreaterThan(0);
        assertThat(records.iterator().next().value()).isEqualTo(testMessage);
    }

}
