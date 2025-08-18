package com.example.aivestorBackend.domain.news.controller;


import com.example.aivestorBackend.domain.news.dto.response.CompanyListDto;
import com.example.aivestorBackend.domain.news.dto.response.NewsDetailsDto;
import com.example.aivestorBackend.domain.news.dto.response.NewsDto;
import com.example.aivestorBackend.domain.news.service.NewsService;
import com.example.aivestorBackend.domain.slackbot.dto.CompanyInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NewsController {
    private final NewsService newsService;

    @GetMapping("/news/by-date")
    public ResponseEntity<Map<String, List<NewsDto>>> getNewsByDate(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ){
        Map<String, List<NewsDto>> result = newsService.getNewsByDate(date);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/news/top")
    public ResponseEntity<Map<String, List<NewsDto>>> getImportantNews(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        Map<String, List<NewsDto>> result = newsService.getTopNewsByDate(date);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/news/list")
    public ResponseEntity<List<NewsDto>> getTopicImportantNews(
            @RequestParam("topic") String topic
    ) {
        List<NewsDto> result = newsService.getImportantNewsByTopic(topic);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/news/detail")
    public NewsDetailsDto getNewsDetails(@RequestParam("newsId") Long newsId){
        return newsService.getNewsDetails(newsId);
    }

    @GetMapping("/companies/list")
    public ResponseEntity<List<String>> getCompanyList(){
        return ResponseEntity.ok(newsService.getCompanyList());
    }

    @GetMapping("/companies/Info")
    public ResponseEntity<List<NewsDto>> getCompanyInfo(@RequestParam("company") String company){
        return ResponseEntity.ok(newsService.getCompanyInfo(company));
    }
}
