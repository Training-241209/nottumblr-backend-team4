package com.team4.nottumblr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.nottumblr.model.Reblogs;

public interface ReblogsRepository extends JpaRepository<Reblogs, Integer> {
    List<Reblogs> findByBlog_BlogId(int blogId);
    Optional<Reblogs> findById(int reblogId);
}
