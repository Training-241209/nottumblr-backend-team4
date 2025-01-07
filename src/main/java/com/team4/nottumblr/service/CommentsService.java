package com.team4.nottumblr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Comments;
import com.team4.nottumblr.repository.CommentsRepository;

@Service
public class CommentsService {
    
    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private JwtService jwtService;

    public List<Comments> getAllCommentsByPost(int post_id) {
        return commentsRepository.findByPost_Post_id(post_id);
    }

    public Comments createComment(Comments comment) {
        if (comment.getPost() == null || comment.getPost().getPost_id() <= 0) {
            throw new IllegalArgumentException("Comment must be associated with a valid post.");
        }
    
        if (comment.getBlogger() == null || comment.getBlogger().getBlogger_id() <= 0) {
            throw new IllegalArgumentException("Comment must be associated with a valid blogger.");
        }

        if (comment.getContent() == null || comment.getContent().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty.");
        }
    
        return commentsRepository.save(comment);
    }

    public void deleteComment(int comment_id, int post_id, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Comments comment = commentsRepository.findById(comment_id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with ID: " + comment_id));

        if (comment.getPost().getPost_id() != post_id) {
            throw new IllegalArgumentException("Comment does not belong to the specified post.");
        }

        boolean isOwner = comment.getBlogger().getBlogger_id() == currentBlogger.getBlogger_id();
        boolean isAdmin = currentBlogger.getRole().getRoleName().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException("You are not authorized to delete this comment.");
        }

        commentsRepository.delete(comment);
    }
}
