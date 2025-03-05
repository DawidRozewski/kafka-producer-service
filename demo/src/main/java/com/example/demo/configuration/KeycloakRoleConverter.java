package com.example.demo.configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String CLIENT_ID = "test_client_id";
    private static final String ROLES = "roles";
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        return extractRoles(jwt).stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.toUpperCase()))
                .collect(Collectors.toList());
    }

    private List<String> extractRoles(Jwt jwt) {
        return Optional.ofNullable(jwt.getClaimAsMap(RESOURCE_ACCESS))
                .map(this::getNestedMap)
                .map(this::getRoles)
                .orElse(Collections.emptyList());
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getNestedMap(Map<String, Object> map) {
        return map.getOrDefault(CLIENT_ID, Collections.emptyMap()) instanceof Map
                ? (Map<String, Object>) map.get(CLIENT_ID)
                : Collections.emptyMap();
    }

    @SuppressWarnings("unchecked")
    private List<String> getRoles(Map<String, Object> map) {
        return map.getOrDefault(ROLES, Collections.emptyList()) instanceof List
                ? (List<String>) map.get(ROLES)
                : Collections.emptyList();
    }
    
}
