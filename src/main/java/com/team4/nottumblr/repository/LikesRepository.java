package com.team4.nottumblr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.nottumblr.model.Likes;

public interface LikesRepository extends JpaRepository<Likes, Integer> {
    List<Likes> findByPost_Post_id(int post_id);
    Likes deleteByPost_Post_id(int post_id);
}
