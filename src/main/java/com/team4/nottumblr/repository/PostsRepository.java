package com.team4.nottumblr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.nottumblr.model.Posts;

public interface PostsRepository extends JpaRepository<Posts, Integer> {
    List<Posts> findByBlog_Blog_id(int blog_id); 
    List<Posts> findByBlog_Blogger_Blogger_id(long blogger_id); 
}
