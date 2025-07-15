package com.example.aivestorBackend.domain.news.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "importantnews")
public class ImportantNews {
    @Id
    @Column(name = "news_id")
    private Long newsId;

    private String topic;

    @Column(name = "news_date", nullable = false)
    private LocalDate newsDate;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", insertable = false, updatable = false)
    private News news;
}
