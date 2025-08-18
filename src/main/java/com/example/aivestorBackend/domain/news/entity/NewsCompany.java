package com.example.aivestorBackend.domain.news.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode; // Added

import java.io.Serializable;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "newscompany")
@IdClass(NewsCompany.NewsCompanyId.class)
public class NewsCompany {

    @Id
    @Column(name = "news_id")
    private Long newsId;

    @Id
    private String company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", insertable = false, updatable = false)
    private News news;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode // Added
    public static class NewsCompanyId implements Serializable {
        private Long newsId;
        private String company;
    }
}
