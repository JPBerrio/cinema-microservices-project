package com.microservice.auth.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtConfig {

    private String SECRET_KEY;
    private Algorithm ALGORITHM;

    @Value("${SECRET_WORD}")
    public void setSecretKey(String secretKey) {
        this.SECRET_KEY = secretKey;
    }

    @PostConstruct
    public void init() {
        this.ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    }

    public String createToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuer("auth-0")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)))
                .sign(ALGORITHM);
    }

    public boolean isValid(String jwt) {
        try {
            JWT.require(ALGORITHM)
                    .build()
                    .verify(jwt);
            return true;
        }catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getUsername(String jwt) {
        return JWT.require(ALGORITHM)
                .build()
                .verify(jwt)
                .getSubject();
    }
}