package com.example.demo.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeycloakRoleConverterTest {

    private static final String TEST_CLIENT_ID = "test_client_id";
    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String ROLES = "roles";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";

    @InjectMocks
    private KeycloakRoleConverter keycloakRoleConverter;

    @Mock
    private Jwt jwt;

    private Map<String, Object> resourceAccess;
    private Map<String, Object> testClientId;

    @BeforeEach
    void setUp() {
        resourceAccess = new HashMap<>();
        testClientId = new HashMap<>();
        resourceAccess.put(TEST_CLIENT_ID, testClientId);
    }

    @Test
    void shouldReturnRolesWhenAllDataIsCorrect() {
        // Given
        testClientId.put(ROLES, List.of("admin", "user"));
        when(jwt.getClaimAsMap(RESOURCE_ACCESS)).thenReturn(resourceAccess);

        // When
        Collection<GrantedAuthority> authorities = keycloakRoleConverter.convert(jwt);

        // Then
        assertNotNull(authorities);
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority(ROLE_ADMIN)));
        assertTrue(authorities.contains(new SimpleGrantedAuthority(ROLE_USER)));
    }

    @Test
    void shouldReturnEmptyListWhenResourceAccessIsNull() {
        // Given
        when(jwt.getClaimAsMap(RESOURCE_ACCESS)).thenReturn(null);

        // When
        Collection<GrantedAuthority> authorities = keycloakRoleConverter.convert(jwt);

        // Then
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenClientIdIsNull() {
        // Given
        resourceAccess.put(TEST_CLIENT_ID, null);
        when(jwt.getClaimAsMap(RESOURCE_ACCESS)).thenReturn(resourceAccess);

        // When
        Collection<GrantedAuthority> authorities = keycloakRoleConverter.convert(jwt);

        // Then
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenClientIdIsNotAMap() {
        // Given
        resourceAccess.put(TEST_CLIENT_ID, "not_a_map");
        when(jwt.getClaimAsMap(RESOURCE_ACCESS)).thenReturn(resourceAccess);

        // When
        Collection<GrantedAuthority> authorities = keycloakRoleConverter.convert(jwt);

        // Then
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenRolesAreNull() {
        // Given
        testClientId.put(ROLES, null);
        when(jwt.getClaimAsMap(RESOURCE_ACCESS)).thenReturn(resourceAccess);

        // When
        Collection<GrantedAuthority> authorities = keycloakRoleConverter.convert(jwt);

        // Then
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenRolesAreNotAList() {
        // Given
        testClientId.put(ROLES, "not_a_list");
        when(jwt.getClaimAsMap(RESOURCE_ACCESS)).thenReturn(resourceAccess);

        // When
        Collection<GrantedAuthority> authorities = keycloakRoleConverter.convert(jwt);

        // Then
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenRolesAreEmpty() {
        // Given
        testClientId.put(ROLES, List.of());
        when(jwt.getClaimAsMap(RESOURCE_ACCESS)).thenReturn(resourceAccess);

        // When
        Collection<GrantedAuthority> authorities = keycloakRoleConverter.convert(jwt);

        // Then
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }
}