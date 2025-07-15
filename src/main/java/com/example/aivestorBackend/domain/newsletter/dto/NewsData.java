package com.example.aivestorBackend.domain.newsletter.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class NewsData {
    private int id;
    private String title;
    private String source;
    private String summary;
    private String thumbnailUrl;
    private String url;
    private List<String> companies;
    private Map<String, CompanyInfo> companiesInfo;

    @Data
    public static class CompanyInfo {
        private String change;
        private String changePercent;
        private boolean isPositive;
        private String price;
    }
}
