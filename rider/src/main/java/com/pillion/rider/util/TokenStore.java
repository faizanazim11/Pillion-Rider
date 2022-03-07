package com.pillion.rider.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class TokenStore {

    public final Map<String, Authentication> cache = new HashMap<>();

    public String generateToken(String access, Authentication authentication) {
        String token = access.replace('.', ' ');
        cache.put(token, authentication);
        return token;
    }

    public Authentication getAuthentication(String token) {
        return cache.getOrDefault(token.replace('.', ' '), null);
    }

}
