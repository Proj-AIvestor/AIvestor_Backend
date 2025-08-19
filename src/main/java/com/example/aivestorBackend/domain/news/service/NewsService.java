package com.example.aivestorBackend.domain.news.service;

import org.springframework.transaction.annotation.Transactional;
import com.example.aivestorBackend.domain.news.config.NewsProperties;
import com.example.aivestorBackend.domain.news.dto.response.NewsDetailsDto;
import com.example.aivestorBackend.domain.news.dto.response.NewsDto;
import com.example.aivestorBackend.domain.news.entity.ImportantNews;
import com.example.aivestorBackend.domain.news.entity.News;
import com.example.aivestorBackend.domain.news.entity.NewsCompany;
import com.example.aivestorBackend.domain.news.repository.ImportantNewsRepository;
import com.example.aivestorBackend.domain.news.repository.NewsRepository;
import com.example.aivestorBackend.domain.news.repository.NewsCompanyRepository; // Added
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class NewsService {
    private final ImportantNewsRepository importantNewsRepository;
    private final NewsRepository newsRepository;
    private final NewsCompanyRepository newsCompanyRepository; // Added
    private final NewsProperties newsProperties;

    // New method for getCompanyList API
    public List<String> getCompanyList() {
        return newsCompanyRepository.findDistinctCompany();
    }

    // New method for getCompanyInfo API
    public List<NewsDto> getCompanyInfo(String company) {
        return newsCompanyRepository.findByCompany(company).stream()
                .map(NewsCompany::getNews) // Get the News entity from NewsCompany
                .filter(java.util.Objects::nonNull) // Ensure News entity is not null
                .sorted((n1, n2) -> n2.getPublicationDate().compareTo(n1.getPublicationDate())) // Add sorting here
                .map(this::toNewsDto) // Map News entity to NewsDto
                .collect(Collectors.toList());
    }

    /**
     * @return topic → [TopNewsDto,…]
     */
    public Map<String, List<NewsDto>> getTopNewsByDate(LocalDate date) {
        return newsProperties.getTopics().stream()
                .collect(Collectors.toMap(
                        // key: 토픽 이름
                        topic -> topic,
                        // value: 해당 토픽·날짜의 뉴스들 → DTO 변환
                        topic -> importantNewsRepository.findByNewsDateAndTopicWithCompanies(date, topic).stream()
                                .map(NewsService::toDto)
                                .collect(Collectors.toList())
                ));
    }

    public List<NewsDto> getImportantNewsByTopic(String topic) {
        return importantNewsRepository.findByTopicWithCompanies(topic).stream()
                .sorted((n1, n2) -> n2.getNews().getPublicationDate().compareTo(n1.getNews().getPublicationDate()))
                .map(NewsService::toDto)
                .collect(Collectors.toList());
    }

    /**
     * ImportantNews 엔티티 + 연관 News 엔티티 → DTO 변환
     */
    private static NewsDto toDto(ImportantNews news) {
        News n = news.getNews();
        List<String> companies = n.getNewsCompanies().stream()
                .map(NewsCompany::getCompany)
                .collect(Collectors.toList());
        return new NewsDto(
                n.getId(),
                n.getTitle(),
                n.getSummary(),
                n.getSource(),
                n.getUrl(),
                news.getThumbnailUrl(),
                news.getTtsUrl(),
                n.getPublicationDate(),
                companies
        );
    }

    public NewsDetailsDto getNewsDetails(Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("News not found with id: " + newsId));

        ImportantNews importantNews = importantNewsRepository.findByNews_Id(newsId);

        List<String> relativeCompanies = news.getNewsCompanies().stream()
                .map(NewsCompany::getCompany)
                .collect(Collectors.toList());

        String thumbnailUrl = (importantNews != null) ? importantNews.getThumbnailUrl() : null;

        return new NewsDetailsDto(
                news.getId(),
                news.getTitle(),
                news.getSummary(),
                news.getContent(),
                news.getSource(),
                news.getTopic(),
                news.getUrl(),
                news.getPublicationDate(),
                relativeCompanies,
                thumbnailUrl
        );
    }

    public Map<String, List<NewsDto>> getNewsByDate(LocalDate date) {
        return newsProperties.getTopics().stream()
                .collect(Collectors.toMap(
                        topic -> topic,
                        topic -> newsRepository.findByPublicationDateAndTopicWithCompanies(date, topic).stream()
                                .map(this::toNewsDto)
                                .collect(Collectors.toList())
                ));
    }

    private NewsDto toNewsDto(News news) {
        List<String> companies = news.getNewsCompanies().stream()
                .map(NewsCompany::getCompany)
                .collect(Collectors.toList());

        ImportantNews importantNews = importantNewsRepository.findByNews_Id(news.getId());
        String thumbnailUrl = (importantNews != null) ? importantNews.getThumbnailUrl() : null;
        String ttsUrl = (importantNews != null) ? importantNews.getTtsUrl() : null;

        return new NewsDto(
                news.getId(),
                news.getTitle(),
                news.getSummary(),
                news.getSource(),
                news.getUrl(),
                thumbnailUrl,
                ttsUrl,
                news.getPublicationDate(),
                companies
        );
    }
}
