package com.example.demo.controller;

import com.example.demo.service.KafkaProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class KafkaControllerTest {
    private final String USER_ACCESS_ENDPOINT = "/producer/user";
    private final String ADMIN_ACCESS_ENDPOINT = "/producer/admin";
    private static final String SEND_MESSAGE_ENDPOINT = "/producer/send";

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

}