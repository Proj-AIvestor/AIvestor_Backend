package com.example.aivestorBackend.domain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                // 프론트에서 호출할 모든 /api/** 엔드포인트에 대해
                .addMapping("/api/**")
                // Vite dev 서버 주소
                .allowedOrigins("http://localhost:5173")
                // 허용할 HTTP 메서드
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 허용할 헤더
                .allowedHeaders("*")
                // 자격증명(cookie) 전송 허용이 필요하면 true
                .allowCredentials(true);
    }
}
