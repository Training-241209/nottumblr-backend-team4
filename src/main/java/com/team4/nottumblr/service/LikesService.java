package com.team4.nottumblr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.dto.LikesDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Likes;
import com.team4.nottumblr.model.Posts;
import com.team4.nottumblr.model.Reblogs;
import com.team4.nottumblr.repository.LikesRepository;
import com.team4.nottumblr.repository.PostsRepository;
import com.team4.nottumblr.repository.ReblogsRepository;

@Service
public class LikesService {

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private ReblogsRepository reblogsRepository;

    @Autowired
    private JwtService jwtService;

    // Likes for Posts
    public List<LikesDTO> getAllLikesByPost(int postId) {
        List<Likes> likes = likesRepository.findByPost_PostId(postId);
        return likes.stream()
                .map(like -> new LikesDTO(
                        like.getLikeId(),
                        like.getBlogger().getUsername(),
                        like.getPost().getPostId()))
                .toList();
    }

    public LikesDTO createLikeForPost(int postId, String token) {
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

    public void deleteLikeForPost(int postId, int likeId, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Likes like = likesRepository.findById(likeId)
                .orElseThrow(() -> new IllegalArgumentException("Like not found with ID: " + likeId));

        if (like.getPost() == null || like.getPost().getPostId() != postId) {
            throw new IllegalArgumentException("Like does not belong to the specified post.");
        }

        if (like.getBlogger().getBloggerId() != currentBlogger.getBloggerId()) {
            throw new IllegalArgumentException("You are not authorized to delete this like.");
        }

        likesRepository.delete(like);
    }

    // Likes for Reblogs
    public LikesDTO createLikeForReblog(int reblogId, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Reblogs reblog = reblogsRepository.findById(reblogId)
                .orElseThrow(() -> new IllegalArgumentException("Reblog not found with ID: " + reblogId));

        if (likesRepository.existsByReblog_ReblogIdAndBlogger_BloggerId(reblogId, currentBlogger.getBloggerId())) {
            throw new IllegalArgumentException("You have already liked this reblog.");
        }

        Likes like = new Likes();
        like.setReblog(reblog);
        like.setBlogger(currentBlogger);
        Likes savedLike = likesRepository.save(like);

        return new LikesDTO(
                savedLike.getLikeId(),
                currentBlogger.getUsername(),
                reblogId
        );
    }

    public void deleteLikeForReblog(int reblogId, int likeId, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        Likes like = likesRepository.findById(likeId)
                .orElseThrow(() -> new IllegalArgumentException("Like not found with ID: " + likeId));

        if (like.getReblog() == null || like.getReblog().getReblogId() != reblogId) {
            throw new IllegalArgumentException("Like does not belong to the specified reblog.");
        }

        if (like.getBlogger().getBloggerId() != currentBlogger.getBloggerId()) {
            throw new IllegalArgumentException("You are not authorized to delete this like.");
        }

        likesRepository.delete(like);
    }

    public List<LikesDTO> getAllLikesByReblog(int reblogId) {
        // Fetch all likes for the specified reblog
        List<Likes> likes = likesRepository.findByReblog_ReblogId(reblogId);
    
        // Map the Likes entities to LikesDTO
        return likes.stream()
                .map(like -> new LikesDTO(
                        like.getLikeId(),
                        like.getBlogger().getUsername(), 
                        like.getReblog().getReblogId()  
                ))
                .toList();
    }
}
