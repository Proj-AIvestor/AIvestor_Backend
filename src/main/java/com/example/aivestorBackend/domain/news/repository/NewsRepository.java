package com.example.aivestorBackend.domain.news.repository;

import com.example.aivestorBackend.domain.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long>  {
}
