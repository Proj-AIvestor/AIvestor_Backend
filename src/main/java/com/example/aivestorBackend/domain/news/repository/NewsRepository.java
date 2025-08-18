package com.example.aivestorBackend.domain.news.repository;

import com.example.aivestorBackend.domain.news.entity.ImportantNews;
import com.example.aivestorBackend.domain.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long>  {
    @Query("SELECT n FROM News n LEFT JOIN FETCH n.newsCompanies WHERE n.publicationDate = :date AND n.topic = :topic")
    List<News> findByPublicationDateAndTopicWithCompanies(@Param("date") LocalDate date, @Param("topic") String topic);
}
