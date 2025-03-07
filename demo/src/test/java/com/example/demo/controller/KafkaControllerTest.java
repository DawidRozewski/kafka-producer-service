package com.example.demo.controller;

import com.example.demo.AbstractConfiguredTest;
import com.example.demo.service.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.kafka.KafkaException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(KafkaControllerTest.KafkaProducerServiceMockConfig.class)
class KafkaControllerTest extends AbstractConfiguredTest {
    private final String USER_ACCESS_ENDPOINT = "/producer/user";
    private final String ADMIN_ACCESS_ENDPOINT = "/producer/admin";

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Test
    @WithMockUser(roles = "USER")
    void givenUserWithUserRole_whenAccessingUserEndpoint_thenShouldGrantAccess() throws Exception {
        mockMvc.perform(get(USER_ACCESS_ENDPOINT))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void givenUserWithAdminRole_whenAccessingAdminEndpoint_thenShouldGrantAccess() throws Exception {
        mockMvc.perform(get(ADMIN_ACCESS_ENDPOINT))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void givenUserWithAdminRole_whenAccessingUserEndpoint_thenShouldDenyAccess() throws Exception {
        mockMvc.perform(get(USER_ACCESS_ENDPOINT))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "USER")
    void givenUserWithUserRole_whenAccessingAdminEndpoint_thenShouldDenyAccess() throws Exception {
        mockMvc.perform(get(ADMIN_ACCESS_ENDPOINT))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenAnonymousUser_whenAccessingAdminEndpoint_thenShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get(ADMIN_ACCESS_ENDPOINT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenAnonymousUser_whenAccessingUserEndpoint_thenShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get(USER_ACCESS_ENDPOINT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn500WhenKafkaIsDown() throws Exception {
        // Given
        doThrow(new KafkaException("Kafka is not working"))
                .when(kafkaProducerService).sendMessage(any());

        // When & Then
        mockMvc.perform(post("/producer/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Test message\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Kafka error: Kafka is not working"));
    }

    @TestConfiguration
    static class KafkaProducerServiceMockConfig {
        @Bean
        public KafkaProducerService kafkaProducerService() {
            return mock(KafkaProducerService.class);
        }
    }
}