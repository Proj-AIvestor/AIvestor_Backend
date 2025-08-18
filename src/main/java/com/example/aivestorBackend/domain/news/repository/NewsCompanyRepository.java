package com.example.aivestorBackend.domain.news.repository;

import com.example.aivestorBackend.domain.news.entity.NewsCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsCompanyRepository extends JpaRepository<NewsCompany, NewsCompany.NewsCompanyId> {

    // getCompanyList API를 위한 유니크한 회사 목록 조회
    @Query("SELECT DISTINCT nc.company FROM NewsCompany nc")
    List<String> findDistinctCompany();

    // getCompanyInfo API를 위한 회사 이름으로 NewsCompany 조회
    List<NewsCompany> findByCompany(String company);
}
