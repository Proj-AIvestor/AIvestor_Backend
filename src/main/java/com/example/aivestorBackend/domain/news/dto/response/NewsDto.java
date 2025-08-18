package com.example.aivestorBackend.domain.news.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class NewsDto {
    private Long id;
    private String title;
    private String summary;
    private String source;
    private String url;
    private String thumbnailUrl;
    private String ttsUrl;
    private LocalDate publicationDate;
    private List<String> companies;
}
