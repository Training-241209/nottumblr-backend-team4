package com.team4.nottumblr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.nottumblr.model.Blogs;

public interface BlogsRepository extends JpaRepository<Blogs, Integer> {
    
}
