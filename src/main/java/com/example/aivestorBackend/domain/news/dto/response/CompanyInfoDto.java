package com.example.aivestorBackend.domain.news.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInfoDto {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private String source;
    private String topic;
    private String url;
    private LocalDate publicationDate;
    private List<String> companies;
    private String thumbnailUrl;
}