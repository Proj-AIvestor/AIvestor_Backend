package com.example.aivestorBackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class CorsConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                .allowedOriginPatterns("*") // 모든 Origin 허용
                .allowedMethods("*") // 모든 HTTP Method (GET, POST 등) 허용
                .allowedHeaders("*") // 모든 Header 허용
                .allowCredentials(true) // Credentials (쿠키 등) 허용
                .maxAge(3600); // pre-flight 캐싱 시간 (초)
    }
}