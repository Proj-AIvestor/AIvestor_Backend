package com.example.aivestorBackend.domain.slackbot.controller;

import com.example.aivestorBackend.domain.slackbot.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slack")
@RequiredArgsConstructor
public class SlackController {
    private final SlackService slackService;

    @PostMapping("/sendTopNews")
    public ResponseEntity<String> postTopNews() {
        try {
            slackService.sendTopNews();
            return ResponseEntity.ok("메시지 전송 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("전송 실패: " + e.getMessage());
        }
    }
}

