package com.team4.nottumblr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.nottumblr.model.Likes;

public interface LikesRepository extends JpaRepository<Likes, Integer> {
    // Retrieve likes
    List<Likes> findByPost_PostId(int postId);
    Optional<Likes> findByPost_PostIdAndBlogger_BloggerId(int postId, long bloggerId);

    // Check existence
    boolean existsByPost_PostIdAndBlogger_BloggerId(int postId, long bloggerId);
    boolean existsByPost_PostId(int postId);

    // Delete likes
    void deleteByPost_PostIdAndBlogger_BloggerId(int postId, long bloggerId);
    void deleteByPost_PostId(int postId);

    boolean existsByReblog_ReblogIdAndBlogger_BloggerId(int reblogId, long bloggerId);
    List<Likes> findByReblog_ReblogId(int reblogId);

    int countByPost_PostId(int postId);
}
