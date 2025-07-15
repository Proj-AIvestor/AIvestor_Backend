package com.example.aivestorBackend.domain.slackbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class NewsDataDto {
    private String title;
    private String summary;
    private String source;
    private String url;
    private List<String> companies;
    @JsonProperty("companiesInfo")
    private Map<String, CompanyInfoDto> companiesInfo;
}
