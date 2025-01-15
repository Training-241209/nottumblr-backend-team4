package com.team4.nottumblr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.dto.ReblogsDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Posts;
import com.team4.nottumblr.model.Reblogs;
import com.team4.nottumblr.repository.BloggersRepository;
import com.team4.nottumblr.repository.PostsRepository;
import com.team4.nottumblr.repository.ReblogsRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ReblogsService {
    @Autowired
    private ReblogsRepository reblogsRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private BloggersRepository bloggersRepository;

    // Get all reblogs for a specific post
    public List<ReblogsDTO> getAllReblogsByPostId(int postId) {
        // Validate if the post exists
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with ID " + postId + " not found."));

        // Fetch all reblogs for the given post and map to DTOs
        return reblogsRepository.findByPost_PostId(postId).stream()
                .map(reblog -> new ReblogsDTO(
                        reblog.getReblogId(),
                        reblog.getComment(),
                        reblog.getRebloggedAt().toString(),
                        reblog.getBlogger().getUsername(),
                        reblog.getBlogger().getProfilePictureUrl(), // Blogger's profile picture
                        post.getContent(), // Original post content
                        post.getBlogger().getUsername(), // Original post username
                        post.getBlogger().getProfilePictureUrl(), // Original post profile picture
                        post.getMediaUrl() // Original post media URL
                ))
                .toList();
    }

    // Create a new reblog
    public ReblogsDTO createReblog(int postId, String comment, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        // Validate if the post exists
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));

        // Create and save the reblog
        Reblogs reblog = new Reblogs(post, currentBlogger, comment);
        Reblogs savedReblog = reblogsRepository.save(reblog);

        // Map to DTO
        return new ReblogsDTO(
            savedReblog.getReblogId(),
            savedReblog.getComment(),
            savedReblog.getRebloggedAt().toString(),
            savedReblog.getBlogger().getUsername(),
            savedReblog.getBlogger().getProfilePictureUrl(), // Blogger's profile picture
            post.getContent(), // Original post content
            post.getBlogger().getUsername(), // Original post username
            post.getBlogger().getProfilePictureUrl(), // Original post profile picture
            post.getMediaUrl() // Original post media URL
        );
    }

    // Delete a reblog
    public void deleteReblog(int reblogId, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        // Validate if the reblog exists
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

    // Get all reblogs by the currently authenticated blogger
    public List<ReblogsDTO> getAllReblogsByBlogger(String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        return reblogsRepository.findByBlogger(currentBlogger).stream()
            .map(reblog -> new ReblogsDTO(
                reblog.getReblogId(),
                reblog.getComment(),
                reblog.getRebloggedAt().toString(),
                reblog.getBlogger().getUsername(),
                reblog.getBlogger().getProfilePictureUrl(), // Blogger's profile picture
                reblog.getPost() != null ? reblog.getPost().getContent() : null,
                reblog.getPost() != null ? reblog.getPost().getBlogger().getUsername() : null,
                reblog.getPost() != null ? reblog.getPost().getBlogger().getProfilePictureUrl() : null,
                reblog.getPost() != null ? reblog.getPost().getMediaUrl() : null
            ))
            .toList();
    }

    public List<ReblogsDTO> getAllReblogsByOtherBlogger(Long bloggerId) {
        Bloggers otherBlogger = bloggersRepository.findById(bloggerId)
            .orElseThrow(() -> new EntityNotFoundException("Blogger not found"));
        
        return reblogsRepository.findByBlogger(otherBlogger).stream()
            .map(reblog -> new ReblogsDTO(
                reblog.getReblogId(),
                reblog.getComment(),
                reblog.getRebloggedAt().toString(),
                reblog.getBlogger().getUsername(),
                reblog.getBlogger().getProfilePictureUrl(), // Blogger's profile picture
                reblog.getPost() != null ? reblog.getPost().getContent() : null,
                reblog.getPost() != null ? reblog.getPost().getBlogger().getUsername() : null,
                reblog.getPost() != null ? reblog.getPost().getBlogger().getProfilePictureUrl() : null,
                reblog.getPost() != null ? reblog.getPost().getMediaUrl() : null
            ))
            .toList();
    }
}
