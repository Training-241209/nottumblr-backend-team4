package com.team4.nottumblr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.dto.LikesDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Likes;
import com.team4.nottumblr.model.Posts;
import com.team4.nottumblr.repository.LikesRepository;
import com.team4.nottumblr.repository.PostsRepository;

@Service
public class LikesService {
    
    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private JwtService jwtService;

    public List<LikesDTO> getAllLikesByPost(int postId) {
        // Fetch all likes for the post
        List<Likes> likes = likesRepository.findByPost_PostId(postId);
        
        // Convert the Likes entities to LikesDTO
        return likes.stream()
                    .map(like -> new LikesDTO(
                        like.getLikeId(),
                        like.getBlogger().getUsername(), // Fetch username from the related Blogger
                        like.getPost().getPostId()       // Fetch postId from the related Post
                    ))
                    .toList();
    }


    public LikesDTO createLike(int postId, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);
    
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));
    
        if (likesRepository.existsByPost_PostIdAndBlogger_BloggerId(postId, currentBlogger.getBloggerId())) {
            throw new IllegalArgumentException("You have already liked this post.");
        }
    
        Likes like = new Likes();
        like.setPost(post);
        like.setBlogger(currentBlogger);
        Likes savedLike = likesRepository.save(like);
    
        return new LikesDTO(
            savedLike.getLikeId(),
            currentBlogger.getUsername(),
            postId
        );
    }


    public void deleteLike(int postId, int likeId, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);
    
        Likes like = likesRepository.findById(likeId)
                .orElseThrow(() -> new IllegalArgumentException("Like not found with ID: " + likeId));
    
        // Verify the like belongs to the correct post
        if (like.getPost().getPostId() != postId) {
            throw new IllegalArgumentException("Like does not belong to the specified post.");
        }
    
        // Verify the current user is the one who liked the post
        if (like.getBlogger().getBloggerId() != currentBlogger.getBloggerId()) {
            throw new IllegalArgumentException("You are not authorized to delete this like.");
        }
    
        likesRepository.delete(like);
    }
    
    
    
}
