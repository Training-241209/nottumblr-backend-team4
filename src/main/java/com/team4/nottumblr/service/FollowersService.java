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

    public List<Followers> getFollowersByBlogId(int blog_id, String token) {
        // Decode the token to validate the blogger
        Bloggers currentBlogger = jwtService.decodeToken(token);

        // Fetch the blog to ensure it exists and belongs to the current blogger
        Blogs blog = blogsRepository.findById(blog_id)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found with ID: " + blog_id));

        if (blog.getBlogger().getBlogger_id() != currentBlogger.getBlogger_id() &&
                !currentBlogger.getRole().getRoleName().equals("ADMIN")) {
            throw new IllegalArgumentException("You are not authorized to view followers for this blog.");
        }

        // Fetch followers for the specified blog
        return followersRepository.findByBlog_Blog_id(blog_id);
    }

    public void followBlog(int blog_id, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Blogs blog = blogsRepository.findById(blog_id)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found with ID: " + blog_id));

        if (blog.getBlogger().getBlogger_id() == currentBlogger.getBlogger_id()) {
            throw new IllegalArgumentException("You cannot follow your own blog.");
        }

        Followers follower = new Followers(currentBlogger, blog);
        followersRepository.save(follower);
    }

    public void unfollowBlog(int blog_id, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Blogs blog = blogsRepository.findById(blog_id)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found with ID: " + blog_id));

        if (blog.getBlogger().getBlogger_id() == currentBlogger.getBlogger_id()) {
            throw new IllegalArgumentException("You cannot unfollow your own blog.");
        }

        Followers follower = followersRepository.findByBlogger_Blogger_idAndBlog_Blog_id(currentBlogger.getBlogger_id(), blog_id);

        followersRepository.delete(follower);
    }
}
