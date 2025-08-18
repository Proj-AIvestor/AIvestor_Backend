package com.example.aivestorBackend.domain.slackbot.config;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackConfig {

    @Value("${slack.bot.test_token}")
    private String botToken;

    @Value("${slack.bot.lab_token}")
    private String labBotToken;

    @Bean
    public MethodsClient methodsClient() {
        return Slack.getInstance().methods(labBotToken);
    }
}
