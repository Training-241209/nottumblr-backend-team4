package com.team4.nottumblr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.dto.CommentsDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Comments;
import com.team4.nottumblr.model.Posts;
import com.team4.nottumblr.model.Reblogs;
import com.team4.nottumblr.repository.CommentsRepository;
import com.team4.nottumblr.repository.PostsRepository;
import com.team4.nottumblr.repository.ReblogsRepository;

@Service
public class CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private ReblogsRepository reblogsRepository;

    @Autowired
    private JwtService jwtService;

    // Get all comments for a specific post
    public List<CommentsDTO> getAllCommentsByPost(int postId) {
        postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with ID: " + postId + " not found."));
        
        List<Comments> comments = commentsRepository.findByPost_PostId(postId);
        return comments.stream()
                .map(comment -> new CommentsDTO(
                        comment.getCommentId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getBlogger().getUsername(),
                        postId))
                .toList();
    }

    // Get all comments for a specific reblog
    public List<CommentsDTO> getAllCommentsByReblog(int reblogId) {
        reblogsRepository.findById(reblogId)
                .orElseThrow(() -> new IllegalArgumentException("Reblog with ID: " + reblogId + " not found."));

        List<Comments> comments = commentsRepository.findByReblog_ReblogId(reblogId);
        return comments.stream()
                .map(comment -> new CommentsDTO(
                        comment.getCommentId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getBlogger().getUsername(),
                        reblogId))
                .toList();
    }

    // Create a comment for a post
    public CommentsDTO createCommentForPost(int postId, Comments comment, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with ID: " + postId + " not found."));

        if (comment.getContent() == null || comment.getContent().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty.");
        }

        comment.setBlogger(currentBlogger);
        comment.setPost(post);

        Comments savedComment = commentsRepository.save(comment);

        return new CommentsDTO(
                savedComment.getCommentId(),
                savedComment.getContent(),
                savedComment.getCreatedAt(),
                currentBlogger.getUsername(),
                postId
        );
    }

    // Create a comment for a reblog
    public CommentsDTO createCommentForReblog(int reblogId, Comments comment, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Reblogs reblog = reblogsRepository.findById(reblogId)
                .orElseThrow(() -> new IllegalArgumentException("Reblog with ID: " + reblogId + " not found."));

        if (comment.getContent() == null || comment.getContent().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty.");
        }

        comment.setBlogger(currentBlogger);
        comment.setReblog(reblog);

        Comments savedComment = commentsRepository.save(comment);

        return new CommentsDTO(
                savedComment.getCommentId(),
                savedComment.getContent(),
                savedComment.getCreatedAt(),
                currentBlogger.getUsername(),
                reblogId
        );
    }

    // Delete a comment for a post or reblog
    public void deleteComment(int commentId, int postId, Integer reblogId, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Comments comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with ID: " + commentId));

        boolean isOwner = comment.getBlogger().getBloggerId() == currentBlogger.getBloggerId();
        boolean isAdmin = currentBlogger.getRole().getRoleName().equals("ADMIN");

        // Validate whether the comment belongs to the post or reblog
        if (postId != 0 && (comment.getPost() == null || comment.getPost().getPostId() != postId)) {
            throw new IllegalArgumentException("Comment does not belong to the specified post.");
        }

        if (reblogId != null && (comment.getReblog() == null || comment.getReblog().getReblogId() != reblogId)) {
            throw new IllegalArgumentException("Comment does not belong to the specified reblog.");
        }

        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException("You are not authorized to delete this comment.");
        }

        commentsRepository.delete(comment);
    }

    public void deleteCommentByPost(int commentId, int postId, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Comments comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with ID: " + commentId));

        // Validate the comment belongs to the specified post
        if (comment.getPost() == null || comment.getPost().getPostId() != postId) {
            throw new IllegalArgumentException("Comment does not belong to the specified post.");
        }

        boolean isOwner = comment.getBlogger().getBloggerId() == currentBlogger.getBloggerId();
        boolean isAdmin = currentBlogger.getRole().getRoleName().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException("You are not authorized to delete this comment.");
        }

        commentsRepository.delete(comment);
    }

    // Delete a comment for a reblog
    public void deleteCommentByReblog(int commentId, int reblogId, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Comments comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with ID: " + commentId));

        // Validate the comment belongs to the specified reblog
        if (comment.getReblog() == null || comment.getReblog().getReblogId() != reblogId) {
            throw new IllegalArgumentException("Comment does not belong to the specified reblog.");
        }

        boolean isOwner = comment.getBlogger().getBloggerId() == currentBlogger.getBloggerId();
        boolean isAdmin = currentBlogger.getRole().getRoleName().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException("You are not authorized to delete this comment.");
        }

        commentsRepository.delete(comment);
    }
}
