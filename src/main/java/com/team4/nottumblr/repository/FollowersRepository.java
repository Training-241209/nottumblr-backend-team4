package com.team4.nottumblr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.nottumblr.model.Followers;

public interface FollowersRepository extends JpaRepository<Followers, Integer> {
    List<Followers> findByBlog_BlogId(int blogId);
    Followers findByBlogger_BloggerIdAndBlog_BlogId(Long bloggerId, int blogId);

}
