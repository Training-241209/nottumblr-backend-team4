package com.team4.nottumblr.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.dto.PostsDTO;

import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Posts;

import com.team4.nottumblr.repository.PostsRepository;


@Service
public class PostsService {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private JwtService jwtService;

    public List<PostsDTO> getAllPosts() {
        List<Posts> allPosts = postsRepository.findAllByOrderByCreatedAtDesc();

        return allPosts.stream()
                .map(this::convertToPostsDTO)
                .collect(Collectors.toList());
    }

    public List<PostsDTO> getAllMyPosts(String token) {
        // Decode the current blogger from the token
        Bloggers currentBlogger = jwtService.decodeToken(token);

        // Fetch posts created by the current blogger
        List<Posts> myPosts = postsRepository.findByBlogger(currentBlogger);

        // Convert posts to DTOs and return
        return myPosts.stream()
                .map(this::convertToPostsDTO)
                .collect(Collectors.toList());
    }

    public List<PostsDTO> getPostsByBlogger(Long bloggerId) {
        List<Posts> posts = postsRepository.findByBlogger_BloggerIdOrderByCreatedAtDesc(bloggerId);
        return posts.stream()
            .map(this::convertToPostsDTO)
            .collect(Collectors.toList());
    }

    public PostsDTO getPostById(int postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with ID: " + postId + " not found."));
        return convertToPostsDTO(post);
    }

    private PostsDTO convertToPostsDTO(Posts post) {
        return new PostsDTO(
                post.getPostId(),
                post.getBlogger().getUsername(),
                post.getContent(),
                post.getMediaUrl(),
                post.getMediaType(),
                post.getCreatedAt(),
                post.getBlogger().getBloggerId(),
                post.getBlogger().getProfilePictureUrl());
    }

    public PostsDTO createPost(PostsDTO postDTO, String token) {
        // Decode the current blogger from the token
        Bloggers currentBlogger = jwtService.decodeToken(token);

        // Validate post content
        if (postDTO.getContent() == null || postDTO.getContent().isEmpty()) {
            throw new IllegalArgumentException("Post content cannot be empty.");
        }

        // Create a new post and populate its fields
        Posts newPost = new Posts();
        newPost.setContent(postDTO.getContent());
        newPost.setMediaUrl(postDTO.getMediaUrl());
        newPost.setMediaType(postDTO.getMediaType());
        newPost.setBlogger(currentBlogger); // Directly associate with the blogger

        // Save the post to the database
        Posts savedPost = postsRepository.save(newPost);

        // Return a DTO representation of the saved post
        return new PostsDTO(
                savedPost.getPostId(),
                currentBlogger.getUsername(),
                savedPost.getContent(),
                savedPost.getMediaUrl(),
                savedPost.getMediaType(),
                savedPost.getCreatedAt(),
                currentBlogger.getBloggerId(),
                currentBlogger.getProfilePictureUrl());
    }

    public void deletePost(int postId, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        // Retrieve the post
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with ID: " + postId + " not found."));

        // Get the creator of the post
        Bloggers postCreator = post.getBlogger();

        // Check if the current user is the post creator or an admin
        boolean isAdmin = currentBlogger.getRole().getRoleName().equals("ADMIN");
        if (!isAdmin && postCreator.getBloggerId() != currentBlogger.getBloggerId()) {
            throw new IllegalArgumentException("You are not authorized to delete this post.");
        }

        // Delete the post
        postsRepository.deleteById(postId);
    }

}