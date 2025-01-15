package com.team4.nottumblr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Posts;

public interface PostsRepository extends JpaRepository<Posts, Integer> {
    List<Posts> findByBlog_BlogId(int blogId); 
    List<Posts> findByBlogger(Bloggers blogger);
}
