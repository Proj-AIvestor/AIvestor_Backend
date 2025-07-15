package com.example.aivestorBackend.domain.slackbot.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class NewsApiResponseDto {
    private Map<String, List<NewsDataDto>> newsData = new HashMap<>();

    @JsonAnySetter
    public void setNewsData(String key, List<NewsDataDto> value) {
        newsData.put(key, value);
    }
}
