package com.example.demo.configuration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@RequiredArgsConstructor
public class JwtConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private final KeycloakRoleConverter keycloakRoleConverter;

    @Override
    public JwtAuthenticationToken convert(@NonNull Jwt jwt) {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(keycloakRoleConverter);
        return (JwtAuthenticationToken) jwtAuthenticationConverter.convert(jwt);
    }
}