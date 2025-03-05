package com.example.demo.controller;

import com.example.demo.AbstractConfiguredTest;
import com.example.demo.service.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

}