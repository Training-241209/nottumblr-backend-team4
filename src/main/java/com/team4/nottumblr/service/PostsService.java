package com.team4.nottumblr.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.dto.PostsDTO;
import com.team4.nottumblr.dto.TrendingPostsDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Posts;
import com.team4.nottumblr.repository.CommentsRepository;
import com.team4.nottumblr.repository.LikesRepository;
import com.team4.nottumblr.repository.PostsRepository;


@Service
public class PostsService {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    public List<PostsDTO> getPostByTag(String postTag) {
        List<Posts> posts = postsRepository.findByTags(postTag);
        return posts.stream()
            .map(this::convertToPostsDTO)
            .collect(Collectors.toList());
    }

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
                post.getBlogger().getProfilePictureUrl(),
                post.getTags());
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
                currentBlogger.getProfilePictureUrl(),
                savedPost.getTags());
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

    public TrendingPostsDTO getTrendingPostNoAuth() {
        List<Posts> allPosts = postsRepository.findAll();
        if (allPosts.isEmpty()) return null;
    
        // Convert to a list of TrendingPostsDTO, adding interactions + media info
        List<TrendingPostsDTO> trendingList = allPosts.stream()
            .map(post -> {
                TrendingPostsDTO tDto = new TrendingPostsDTO();
                tDto.setPostId(post.getPostId());
                tDto.setUsername(post.getBlogger().getUsername());
                tDto.setContent(post.getContent());
    
                tDto.setProfilePictureUrl(post.getBlogger().getProfilePictureUrl());
                tDto.setMediaUrl(post.getMediaUrl());
                tDto.setMediaType(post.getMediaType());
    
                // 2) Count likes & comments
                int likeCount = likesRepository.countByPost_PostId(post.getPostId());
                int commentCount = commentsRepository.countByPost_PostId(post.getPostId());
                int reblogCount = 0; // or however you compute reblogs
    
                // 3) Fill out interactions
                tDto.setLikeCount(likeCount);
                tDto.setCommentCount(commentCount);
                tDto.setReblogCount(reblogCount);
                tDto.setTotalInteractions(likeCount + commentCount + reblogCount);
    
                return tDto;
            })
            .collect(Collectors.toList());
    
        // Sort descending by totalInteractions
        return trendingList.stream()
            .sorted((a, b) -> b.getTotalInteractions() - a.getTotalInteractions())
            .findFirst()
            .orElse(null);
    }

}