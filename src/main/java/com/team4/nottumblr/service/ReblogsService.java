package com.team4.nottumblr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.dto.ReblogsDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Blogs;
import com.team4.nottumblr.model.Reblogs;
import com.team4.nottumblr.repository.BlogsRepository;
import com.team4.nottumblr.repository.ReblogsRepository;

@Service
public class ReblogsService {

    @Autowired
    private ReblogsRepository reblogsRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BlogsRepository blogsRepository;

    // Get all reblogs for a specific post
    public List<ReblogsDTO> getAllReblogsByBlogId(int blogId) {
        // Validate if the blog exists
        blogsRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog with ID " + blogId + " not found."));
    
        // Fetch all reblogs for the given blog and map to DTOs
        return reblogsRepository.findByBlog_BlogId(blogId).stream()
                .map(reblog -> new ReblogsDTO(
                        reblog.getReblogId(),
                        reblog.getComment(),
                        reblog.getRebloggedAt().toString(),
                        reblog.getBlogger().getUsername(),
                        reblog.getBlog().getTitle()
                ))
                .toList();
    }
    

    public ReblogsDTO createReblog(int blogId, String comment, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);
    
        Blogs blog = blogsRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found with ID: " + blogId));
    
        Reblogs reblog = new Reblogs(blog, currentBlogger, comment);
    
        Reblogs savedReblog = reblogsRepository.save(reblog);
    
        // Map to DTO
        return new ReblogsDTO(
            savedReblog.getReblogId(),
            savedReblog.getComment(),
            savedReblog.getRebloggedAt().toString(),
            savedReblog.getBlogger().getUsername(),
            savedReblog.getBlog().getTitle()
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

