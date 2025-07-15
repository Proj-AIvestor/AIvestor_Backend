package com.example.aivestorBackend.domain.news.repository;

import com.example.aivestorBackend.domain.news.entity.ImportantNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;

public interface ImportantNewsRepository extends JpaRepository<ImportantNews, Long> {

    @Query("SELECT DISTINCT i FROM ImportantNews i JOIN FETCH i.news n LEFT JOIN FETCH n.newsCompanies nc WHERE i.newsDate = :newsDate AND i.topic = :topic")
    List<ImportantNews> findByNewsDateAndTopicWithCompanies(
            @Param("newsDate") LocalDate newsDate,
            @Param("topic") String topic
    );

    @Query("SELECT DISTINCT i FROM ImportantNews i JOIN FETCH i.news n LEFT JOIN FETCH n.newsCompanies nc WHERE i.topic = :topic")
    List<ImportantNews> findByTopicWithCompanies(@Param("topic") String topic);

    ImportantNews findByNews_Id(Long newsId);

}
