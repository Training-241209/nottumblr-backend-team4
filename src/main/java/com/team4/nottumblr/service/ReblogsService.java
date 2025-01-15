package com.team4.nottumblr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.dto.ReblogsDTO;
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
    public List<ReblogsDTO> getAllReblogsByPostId(int postId) {
        // Validate if the post exists
        postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with ID " + postId + " not found."));

        // Fetch all reblogs for the given post and map to DTOs
        return reblogsRepository.findByPost_PostId(postId).stream()
                .map(reblog -> new ReblogsDTO(
                        reblog.getReblogId(),
                        reblog.getComment(),
                        reblog.getRebloggedAt().toString(),
                        reblog.getBlogger().getUsername(),
                        reblog.getPost().getContent(), // Original post content
                        reblog.getPost().getBlogger().getUsername() // Original post username
                ))
                .toList();
    }

    public ReblogsDTO createReblog(int postId, String comment, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));

        Reblogs reblog = new Reblogs(post, currentBlogger, comment);

        Reblogs savedReblog = reblogsRepository.save(reblog);

        // Map to DTO
        return new ReblogsDTO(
            savedReblog.getReblogId(),
            savedReblog.getComment(),
            savedReblog.getRebloggedAt().toString(),
            savedReblog.getBlogger().getUsername(),
            savedReblog.getPost().getContent(), // Original post content
            savedReblog.getPost().getBlogger().getUsername() // Original post username
        );
    }

    public void deleteReblog(int reblogId, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Reblogs reblog = reblogsRepository.findById(reblogId)
                .orElseThrow(() -> new IllegalArgumentException("Reblog not found with ID: " + reblogId));

        // Check if the current blogger is either the reblog creator or an admin
        boolean isAdmin = currentBlogger.getRole().getRoleName().equals("ADMIN");
        boolean isReblogCreator = reblog.getBlogger().getBloggerId() == currentBlogger.getBloggerId();

        if (!isAdmin && !isReblogCreator) {
            throw new IllegalArgumentException("You are not authorized to delete this reblog.");
        }

        reblogsRepository.delete(reblog);
    }
}