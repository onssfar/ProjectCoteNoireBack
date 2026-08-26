package com.cotenoire.config;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig {
    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            public void addCorsMappings(CorsRegistry r) {
                r.addMapping("/api/**").allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173").allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS").allowedHeaders("*");
            }
        };
    }
}
