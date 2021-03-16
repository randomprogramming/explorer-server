package com.randomprogramming.explorer.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.access-token-secret}")
    private String accessTokenSecret;

    @Value("${security.jwt.token.access-token-life}")
    private int accessTokenLife;

    @PostConstruct
    protected void init() {
        // Encode the access token secret into b64, just for some extra security
        accessTokenSecret = Base64.getEncoder().encodeToString(accessTokenSecret.getBytes());
    }
}
