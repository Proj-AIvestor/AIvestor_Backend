package com.example.aivestorBackend.domain.newsletter.service;

import com.example.aivestorBackend.domain.newsletter.config.NewsletterProperties;
import com.example.aivestorBackend.domain.newsletter.dto.NewsData;
import com.example.aivestorBackend.domain.newsletter.entity.User;
import com.example.aivestorBackend.domain.newsletter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsletterService {

    private final WebClient webClient;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final NewsletterProperties newsletterProperties;

    private final UserRepository userRepository;

    // 매일 오전 8시에 뉴스레터 발송 (예시)
    @Scheduled(cron = "0 0 9 * * *")
    public void sendDailyNewsletter() {
        LocalDate specificDate = LocalDate.of(2025, 7, 22);
        LocalDate yesterday = LocalDate.now(); //.minusDays(1);
        String date = specificDate.format(DateTimeFormatter.ISO_DATE);
        String apiUrl = newsletterProperties.getApi().getUrl() + "?date=" + date;

        Map<String, List<NewsData>> newsApiResponseMap = webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, List<NewsData>>>() {})
                .block(); // 비동기 호출을 동기적으로 처리

        List<NewsData> newsList = new ArrayList<>();
        if (newsApiResponseMap != null) {
            newsApiResponseMap.values().forEach(newsList::addAll);
        }

        if (!newsList.isEmpty()) {
            String subject = "[Aivestor] " + date + " 주요 뉴스레터";
            List<User> users = userRepository.findByGetletter(true);
            for(User user : users) {
                String recipient = user.getEmail();
                String htmlContent = generateNewsletterHtml(newsList, newsletterProperties.getFront().getUrl());
                sendEmail(recipient, subject, htmlContent);
                log.info("뉴스레터 발송: {}", user.getUsername());
            }
            log.info("뉴스레터 발송 완료: {}", date);
        } else {
            log.warn("{} 날짜의 뉴스 데이터가 없습니다. 뉴스레터를 발송하지 않습니다.", date);
        }
    }


    private String generateNewsletterHtml(List<NewsData> newsList, String frontUrl) {
        Context context = new Context();
        context.setVariable("newsList", newsList);
        context.setVariable("frontUrl", frontUrl);
        return templateEngine.process("newsletter2", context);
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            // 네이버 메일 호환성을 위해 multipart=false로 설정 (단순 HTML 메시지)
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            // 발송자 정보 설정 (필요시)
            helper.setFrom("noreply@aivestor.com", "AIvestor");

            helper.setTo(to);
            helper.setSubject(subject);

            // HTML 콘텐츠 설정
            helper.setText(htmlContent, true);

            // 네이버 메일 호환성을 위한 명시적 헤더 설정
            message.setHeader("Content-Type", "text/html; charset=UTF-8");
            message.setHeader("Content-Transfer-Encoding", "base64");
            message.setHeader("MIME-Version", "1.0");

            // 추가 호환성 헤더
            message.setHeader("X-Mailer", "AIvestor Newsletter System");

            mailSender.send(message);
            log.info("이메일 발송 성공: {}", to);
        } catch (MessagingException e) {
            log.error("이메일 발송 실패: {}", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void addNewEmail(String email) {
        Optional<User> existingUserOpt = userRepository.findByEmail(email);
        if (existingUserOpt.isEmpty()) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(email);
            newUser.setGetletter(true);
            userRepository.save(newUser);
            log.info("새로운 뉴스레터 구독 이메일 추가: {}", email);
        } else {
            User existingUser = existingUserOpt.get();
            if (!existingUser.getGetletter()) {
                existingUser.setGetletter(true);
                userRepository.save(existingUser);
                log.info("기존 사용자 뉴스레터 구독 상태 업데이트: {}", email);
            } else {
                log.info("이미 뉴스레터를 구독 중인 이메일: {}", email);
            }
        }
    }
}
