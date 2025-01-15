package com.team4.nottumblr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.nottumblr.model.Comments;

public interface CommentsRepository extends JpaRepository<Comments, Integer> {
    List<Comments> findByPost_PostId(int postId);
    List<Comments> findByReblog_ReblogId(int reblogId);
}
