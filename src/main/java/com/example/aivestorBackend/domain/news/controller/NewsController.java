package com.example.aivestorBackend.domain.news.controller;


import com.example.aivestorBackend.domain.news.dto.response.NewsDetailsDto;
import com.example.aivestorBackend.domain.news.dto.response.TopNewsDto;
import com.example.aivestorBackend.domain.news.entity.News;
import com.example.aivestorBackend.domain.news.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {
    private final NewsService newsService;

    @GetMapping("/top")
    public ResponseEntity<Map<String, List<TopNewsDto>>> getImportantNews(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        Map<String, List<TopNewsDto>> result = newsService.getTopNewsByDate(date);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/list")
    public ResponseEntity<List<TopNewsDto>> getTopicImportantNews(
            @RequestParam("topic") String topic
    ) {
        List<TopNewsDto> result = newsService.getImportantNewsByTopic(topic);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/detail")
    public NewsDetailsDto getNewsDetails(@RequestParam("newsId") Long newsId){
        return newsService.getNewsDetails(newsId);
    }

}
