package com.team4.nottumblr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Blogs;

public interface BlogsRepository extends JpaRepository<Blogs, Integer> {
    List<Blogs> findByBlogger(Bloggers blogger);
}
