package com.example.aivestorBackend.domain.news.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TopNewsDto {
    private Long id;
    private String title;
    private String summary;
    private String source;
    private String url;
    private String thumbnailUrl;
    private List<String> companies;
}
