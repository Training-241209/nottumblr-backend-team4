package com.team4.nottumblr.config;

import java.time.Duration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseCookie;

@Configuration
public class CookieConfig {

    public ResponseCookie createJwtCookie(String token) {
        return ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .maxAge(Duration.ofMinutes(15))
                .sameSite("Lax")
                .domain("18.220.70.231")
                .path("/")
                .build();
    }

}
