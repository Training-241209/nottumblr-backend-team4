package com.team4.nottumblr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team4.nottumblr.model.Followers;

public interface FollowersRepository extends JpaRepository<Followers, Integer> {


    // Find all followers for a specific blogger (the blogger being followed)
    List<Followers> findByFollowee_BloggerId(long bloggerId);

    // Find a specific follower-followee relationship
    @Query("SELECT f FROM Followers f WHERE f.follower.bloggerId = :followerId AND f.followee.bloggerId = :bloggerId")
    Optional<Followers> findByFollowerAndFollowee(@Param("followerId") long followerId, @Param("bloggerId") long bloggerId);

    // Check if a follower-followee relationship exists by blogger IDs
    boolean existsByFollower_BloggerIdAndFollowee_BloggerId(long followerId, long bloggerId);
}
