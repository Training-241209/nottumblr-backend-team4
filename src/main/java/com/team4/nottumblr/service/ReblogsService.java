package com.team4.nottumblr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Posts;
import com.team4.nottumblr.model.Reblogs;
import com.team4.nottumblr.repository.PostsRepository;
import com.team4.nottumblr.repository.ReblogsRepository;

@Service
public class ReblogsService {

    @Autowired
    private ReblogsRepository reblogsRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PostsRepository postsRepository;

    // Get all reblogs for a specific post
    public List<Reblogs> getAllReblogsByPost(int post_id) {
        return reblogsRepository.findByPost_Post_id(post_id);
    }

    public Reblogs createReblog(int post_id, String comment, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Posts post = postsRepository.findById(post_id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + post_id));

        Reblogs reblog = new Reblogs(post, currentBlogger, comment);
        return reblogsRepository.save(reblog);
    }


    public void deleteReblog(int reblog_id, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Reblogs reblog = reblogsRepository.findById(reblog_id)
                .orElseThrow(() -> new IllegalArgumentException("Reblog not found with ID: " + reblog_id));

        if (reblog.getBlogger().getBlogger_id() != currentBlogger.getBlogger_id() &&
            !currentBlogger.getRole().getRoleName().equals("ADMIN")) {
            throw new IllegalArgumentException("You are not authorized to delete this reblog.");
        }

        reblogsRepository.delete(reblog);
    }
}

