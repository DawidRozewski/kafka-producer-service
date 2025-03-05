package com.example.demo.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtConverterTest {

    @InjectMocks
    private JwtConverter jwtConverter;

    @Mock
    private Jwt jwt;

    @Mock
    private KeycloakRoleConverter keycloakRoleConverter;

    private static final List<SimpleGrantedAuthority> ROLES = List.of(
            new SimpleGrantedAuthority("ROLE_ADMIN"),
            new SimpleGrantedAuthority("ROLE_USER")
    );

    @Test
    void shouldReturnJwtAuthenticationTokenWhenConvertingJwt() {
        // When
        JwtAuthenticationToken token = jwtConverter.convert(jwt);

        // Then
        assertNotNull(token);
        assertInstanceOf(JwtAuthenticationToken.class, token);
    }

    @Test
    void shouldReturnJwtAuthenticationTokenWithNoAuthoritiesWhenKeycloakRoleConverterReturnsEmptyRoles() {
        // Given
        when(keycloakRoleConverter.convert(jwt)).thenReturn(List.of());

        // When
        JwtAuthenticationToken token = jwtConverter.convert(jwt);

        // Then
        assertNotNull(token);
        assertNotNull(token.getAuthorities());
        assertTrue(token.getAuthorities().isEmpty());
    }

    @Test
    void shouldReturnJwtAuthenticationTokenWithNoAuthoritiesWhenKeycloakRoleConverterIsNotSet() {
        // When
        JwtAuthenticationToken token = jwtConverter.convert(jwt);

        // Then
        assertNotNull(token);
        assertTrue(token.getAuthorities().isEmpty());
    }

    @Test
    void shouldReturnJwtAuthenticationTokenWithAuthoritiesWhenKeycloakRoleConverterReturnsRoles() {
        // Given
        Collection<GrantedAuthority> roles = new ArrayList<>(ROLES);
        when(keycloakRoleConverter.convert(jwt)).thenReturn(roles);

        // When
        JwtAuthenticationToken token = jwtConverter.convert(jwt);

        // Then
        assertNotNull(token);
        assertIterableEquals(ROLES, token.getAuthorities());
    }

    @Test
    void test() {
        // Given
        when(keycloakRoleConverter.convert(jwt)).thenReturn(null);

        // When
        JwtAuthenticationToken token = jwtConverter.convert(jwt);

        // Then
        assertNotNull(token);
        assertTrue(token.getAuthorities().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenJwtIsNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> jwtConverter.convert(null));
    }

}