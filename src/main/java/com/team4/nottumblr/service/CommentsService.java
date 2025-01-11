package com.team4.nottumblr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.dto.CommentsDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Comments;
import com.team4.nottumblr.model.Posts;
import com.team4.nottumblr.repository.CommentsRepository;
import com.team4.nottumblr.repository.PostsRepository;

@Service
public class CommentsService {
    
    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private JwtService jwtService;

    public List<CommentsDTO> getAllCommentsByPost(int postId) {
        // Validate if the post exists
        postsRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Post with ID: " + postId + " not found."));
        
        // Retrieve the comments and convert them to DTOs
        List<Comments> comments = commentsRepository.findByPost_PostId(postId);
        return comments.stream()
                       .map(comment -> new CommentsDTO(
                           comment.getCommentId(),
                           comment.getContent(),
                           comment.getCreatedAt(),
                           comment.getBlogger().getUsername(),
                           postId
                       ))
                       .toList();
    }    
    

    public CommentsDTO createComment(int postId, Comments comment, String token) {
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


    

    public void deleteComment(int commentId, int postId, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Comments comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with ID: " + commentId));

        if (comment.getPost().getPostId() != postId) {
            throw new IllegalArgumentException("Comment does not belong to the specified post.");
        }

        boolean isOwner = comment.getBlogger().getBloggerId() == currentBlogger.getBloggerId();
        boolean isAdmin = currentBlogger.getRole().getRoleName().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new IllegalArgumentException("You are not authorized to delete this comment.");
        }

        commentsRepository.delete(comment);
    }
}
