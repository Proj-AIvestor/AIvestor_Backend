package com.example.aivestorBackend.domain.newsletter.controller;

import com.example.aivestorBackend.domain.newsletter.service.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/newsletter")
@RequiredArgsConstructor
public class NewsletterController {

    private final NewsletterService newsletterService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNewsletterManually() {
        newsletterService.sendDailyNewsletter();
        return ResponseEntity.ok("뉴스레터 발송 요청이 성공적으로 처리되었습니다.");
    }

    @PostMapping("/addEmail")
    public ResponseEntity<String> addNewEmail(
            @RequestParam("email") String email
    ) {
        newsletterService.addNewEmail(email);
        return ResponseEntity.ok("뉴스레터 발송 이메일 추가 요청이 성공적으로 처리되었습니다.");
    }
}
