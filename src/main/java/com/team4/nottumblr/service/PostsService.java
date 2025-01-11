package com.team4.nottumblr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.dto.PostsDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Blogs;
import com.team4.nottumblr.model.Posts;
import com.team4.nottumblr.repository.BlogsRepository;
import com.team4.nottumblr.repository.PostsRepository;

@Service
public class PostsService {
    
    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private BlogsRepository blogsRepository;

    @Autowired
    private JwtService jwtService;

    public List<PostsDTO> getAllPostsFromBlog(int blogId) {
        blogsRepository.findById(blogId)
            .orElseThrow(() -> new IllegalArgumentException("Blog with ID: " + blogId + " not found."));

        List<Posts> posts = postsRepository.findByBlog_BlogId(blogId);
        return posts.stream().map(this::convertToPostsDTO).toList();
    }

    public PostsDTO getPostById(int postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with ID: " + postId + " not found."));
        return convertToPostsDTO(post);
    }

    private PostsDTO convertToPostsDTO(Posts post) {
        return new PostsDTO(
            post.getPostId(),
            post.getBlog().getBlogger().getUsername(),
            post.getContent(),
            post.getMediaUrl(),
            post.getMediaType(),
            post.getCreatedAt(),
            post.getBlog().getBlogId()
        );
    }

    public PostsDTO createPost(PostsDTO postDTO, String token) {
        // Decode the current blogger from the token
        Bloggers currentBlogger = jwtService.decodeToken(token);
    
        // Validate that the blog exists
        Blogs blog = blogsRepository.findById(postDTO.getBlogId())
                .orElseThrow(() -> new IllegalArgumentException("Blog not found with id: " + postDTO.getBlogId()));
    
        // Validate post content
        if (postDTO.getContent() == null || postDTO.getContent().isEmpty()) {
            throw new IllegalArgumentException("Post content cannot be empty.");
        }
    
        // Create the new post and populate its fields
        Posts newPost = new Posts();
        newPost.setContent(postDTO.getContent());
        newPost.setMediaUrl(postDTO.getMediaUrl());
        newPost.setMediaType(postDTO.getMediaType());
        newPost.setBlog(blog);
        newPost.setBlogger(currentBlogger);
    
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
                savedPost.getBlog().getBlogId()
        );
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