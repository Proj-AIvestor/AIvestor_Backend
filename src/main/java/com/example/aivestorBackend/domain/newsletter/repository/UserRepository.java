package com.example.aivestorBackend.domain.newsletter.repository;

import com.example.aivestorBackend.domain.newsletter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByGetletter(boolean getletter);
}
