package com.team4.nottumblr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.nottumblr.model.Followers;

public interface FollowersRepository extends JpaRepository<Followers, Integer> {
    List<Followers> findByBlog_Blog_id(int blog_id);
    Followers findByBlogger_Blogger_idAndBlog_Blog_id(Long blogger_id, int blog_id);

}
