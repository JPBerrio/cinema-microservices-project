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

    @Value("${SECRET_WORD}")
    private String secretKey;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalArgumentException("The Secret cannot be null or empty");
        }
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    public String createToken(String email, String role) {
        return JWT.create()
                .withSubject(email)
                .withClaim("role", "ROLE_" + role)
                .withIssuer("Authentication-Microservices")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)))
                .sign(algorithm);
    }

    public boolean isValid(String jwt) {
        try {
            JWT.require(algorithm)
                    .build()
                    .verify(jwt);
            return true;
        }catch (JWTVerificationException e) {
            System.out.println("Invalid token: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String getUsername(String jwt) {
        return JWT.require(algorithm)
                .build()
                .verify(jwt)
                .getSubject();
    }

    public String getClaim(String jwt, String claimName) {
        return JWT.require(algorithm)
                .build()
                .verify(jwt)
                .getClaim(claimName)
                .asString();
    }
}
