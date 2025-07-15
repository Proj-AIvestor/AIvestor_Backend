package com.example.aivestorBackend.domain.newsletter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "newsletter")
@Getter
@Setter
public class NewsletterProperties {
    private Api api;
    private Front front;

    @Getter
    @Setter
    public static class Api {
        private String url;
    }

    @Getter
    @Setter
    public static class Front {
        private String url;
    }
}
