package com.example.aivestorBackend.domain.slackbot.service;

import com.example.aivestorBackend.domain.slackbot.dto.CompanyInfoDto;
import com.example.aivestorBackend.domain.slackbot.dto.NewsApiResponseDto;
import com.example.aivestorBackend.domain.slackbot.dto.NewsDataDto;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.block.DividerBlock;
import com.slack.api.model.block.HeaderBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.element.ButtonElement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SlackService {

    @Value("${slack.channel.test_channel}")
    private String testChannel;

    @Value("slack.channel.lab_hangout_channel")
    private String labHangoutChannel;

    @Value("${flask.api.url}")
    private String flaskApiUrl;

    @Value("${slack.topic-url}")
    private String topicBaseUrl;

    private final MethodsClient client;
    private final WebClient.Builder webClientBuilder;

    public void sendTopNews() throws IOException, SlackApiException {
        LocalDate testDate = LocalDate.parse("2025-07-04");

        NewsApiResponseDto newsApiResponse = fetchNewsFromApi(testDate);
        List<LayoutBlock> blocks = buildSlackMessageBlocks(newsApiResponse);
        sendMessage(blocks);
    }

    private NewsApiResponseDto fetchNewsFromApi(LocalDate date) {
        String formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);

        NewsApiResponseDto newsApiResponse = webClientBuilder.build()
                .get()
                .uri(flaskApiUrl, uriBuilder -> uriBuilder.queryParam("date", formattedDate).build())
                .retrieve()
                .bodyToMono(NewsApiResponseDto.class)
                .block();

        if (newsApiResponse == null) {
            throw new IllegalStateException("Failed to fetch news from Flask API");
        }
        return newsApiResponse;
    }

    private List<LayoutBlock> buildSlackMessageBlocks(NewsApiResponseDto newsApiResponse) {
        List<LayoutBlock> blocks = new ArrayList<>();

        newsApiResponse.getNewsData().forEach((sector, newsList) -> {
            if (newsList.isEmpty()) {
                return;
            }

            String topicUrl = topicBaseUrl + sector;
            blocks.add(SectionBlock.builder()
                    .text(MarkdownTextObject.builder()
                            .text("*" + sector + " (" + newsList.size() + ")*")
                            .build())
                    .accessory(ButtonElement.builder()
                            .text(PlainTextObject.builder().text("자세히 보기").emoji(true).build())
                            .url(topicUrl)
                            .build())
                    .build());

            blocks.add(new DividerBlock());

            for (NewsDataDto news : newsList) {
                String companiesInfo = news.getCompanies().stream()
                        .map(company -> {
                            CompanyInfoDto info = news.getCompaniesInfo().get(company);
                            if (info == null) return "";
                            String arrow = info.isPositive() ? ":chart_with_upwards_trend:" : ":chart_with_downwards_trend:";
                            return String.format("( %s %s%s%% )", company, arrow, info.getChangePercent());
                        })
                        .collect(Collectors.joining(" "));

                String mdText = String.join("\n",
                        String.format("*<%s|%s>*", news.getUrl(), news.getTitle()),
                        String.format(">%s", news.getSummary()),
                        String.format("*Source*: %s", news.getSource()),
                        String.format("*Related Stocks*: %s", companiesInfo)
                );

                blocks.add(SectionBlock.builder()
                        .text(MarkdownTextObject.builder().text(mdText).build())
                        .build());
            }
        });
        return blocks;
    }

    private void sendMessage(List<LayoutBlock> blocks) throws SlackApiException, IOException {
        if (blocks.isEmpty()) {
            return; // No news to send
        }

        ChatPostMessageResponse resp = client.chatPostMessage(r -> r
                .channel(testChannel)
                .blocks(blocks)
        );

        if (!resp.isOk()) {
            throw new IllegalStateException("Slack API Error: " + resp.getError());
        }
    }
}
