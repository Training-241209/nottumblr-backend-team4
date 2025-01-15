package com.team4.nottumblr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.team4.nottumblr.dto.BloggersDTO;
import com.team4.nottumblr.model.Bloggers;

public interface BloggersRepository extends JpaRepository<Bloggers, Long> { 
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Bloggers findByUsername(String username);
    Optional<Bloggers> findByBloggerId(long bloggerId);


    @Query("SELECT b.bloggerId, b.username, b.profilePictureUrl, COUNT(f) AS followerCount " +
    "FROM Bloggers b LEFT JOIN b.followers f " +
    "GROUP BY b.bloggerId " +
    "ORDER BY followerCount DESC")
    List<Object[]> findTopBloggersWithFollowerCount(int limit);

    List<Bloggers> findByUsernameContainingIgnoreCase(String username);

}
