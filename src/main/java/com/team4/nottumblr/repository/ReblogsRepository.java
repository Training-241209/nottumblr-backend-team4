package com.team4.nottumblr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Reblogs;

public interface ReblogsRepository extends JpaRepository<Reblogs, Integer> {
    List<Reblogs> findByPost_PostId(int postId);
    Optional<Reblogs> findById(int reblogId);
    List<Reblogs> findByBlogger(Bloggers blogger);
}
