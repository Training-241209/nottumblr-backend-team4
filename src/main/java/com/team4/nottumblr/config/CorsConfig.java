package com.team4.nottumblr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173"); // Frontend origin for development

        // config.addAllowedOrigin("https://your-deployed-frontend.com");
        
        config.addAllowedHeader("*");
        config.addExposedHeader("Authorization"); // Expose headers like Authorization
        config.addAllowedMethod("*"); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply this CORS config to all endpoints
        return new CorsFilter(source);
    }
}
