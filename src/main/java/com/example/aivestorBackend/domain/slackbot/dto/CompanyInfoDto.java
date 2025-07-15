package com.example.aivestorBackend.domain.slackbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanyInfoDto {
    private String change;
    @JsonProperty("changePercent")
    private String changePercent;
    @JsonProperty("isPositive")
    private boolean isPositive;
    private String price;
}
