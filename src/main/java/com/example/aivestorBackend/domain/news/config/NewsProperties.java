package com.example.aivestorBackend.domain.news.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "news")
@Getter
public class NewsProperties {
    private List<String> topics = new ArrayList<>();
}