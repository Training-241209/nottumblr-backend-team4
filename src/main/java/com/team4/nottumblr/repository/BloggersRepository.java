package com.team4.nottumblr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.nottumblr.model.Bloggers;

public interface BloggersRepository extends JpaRepository<Bloggers, Long> { 
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Bloggers findByUsername(String username);
    Optional<Bloggers> findByBloggerId(long bloggerId);
}
