package com.team4.nottumblr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Blogs;
import com.team4.nottumblr.model.Followers;
import com.team4.nottumblr.repository.BlogsRepository;
import com.team4.nottumblr.repository.FollowersRepository;

@Service
public class FollowersService {
    @Autowired
    private FollowersRepository followersRepository;

    @Autowired
    private BlogsRepository blogsRepository;

    @Autowired
    private JwtService jwtService;

    public List<Followers> getFollowersByBlogId(int blogId, String token) {
        // Decode the token to validate the blogger
        Bloggers currentBlogger = jwtService.decodeToken(token);

        // Fetch the blog to ensure it exists and belongs to the current blogger
        Blogs blog = blogsRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found with ID: " + blogId));

        if (blog.getBlogger().getBloggerId() != currentBlogger.getBloggerId() &&
                !currentBlogger.getRole().getRoleName().equals("ADMIN")) {
            throw new IllegalArgumentException("You are not authorized to view followers for this blog.");
        }

        // Fetch followers for the specified blog
        return followersRepository.findByBlog_BlogId(blogId);
    }

    public void followBlog(int blogId, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Blogs blog = blogsRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found with ID: " + blogId));

        if (blog.getBlogger().getBloggerId() == currentBlogger.getBloggerId()) {
            throw new IllegalArgumentException("You cannot follow your own blog.");
        }

        Followers follower = new Followers(currentBlogger, blog);
        followersRepository.save(follower);
    }

    public void unfollowBlog(int blogId, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Blogs blog = blogsRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found with ID: " + blogId));

        if (blog.getBlogger().getBloggerId() == currentBlogger.getBloggerId()) {
            throw new IllegalArgumentException("You cannot unfollow your own blog.");
        }

        Followers follower = followersRepository.findByBlogger_BloggerIdAndBlog_BlogId(currentBlogger.getBloggerId(), blogId);

        followersRepository.delete(follower);
    }
}
