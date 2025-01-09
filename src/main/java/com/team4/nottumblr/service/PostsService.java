package com.team4.nottumblr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Posts;
import com.team4.nottumblr.repository.PostsRepository;

@Service
public class PostsService {
    
    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private JwtService jwtService;

    public List<Posts> getAllPosts(String token, Integer blogId, Long bloggerId) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        if(blogId != null) {
            return postsRepository.findByBlog_BlogId(blogId);
        }

        if(bloggerId != null) {
            return postsRepository.findByBlog_Blogger_BloggerId(bloggerId);
        }

        return postsRepository.findAll();
    }

    public Posts getPostById(int postId, String token) {
        return postsRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Can't Find ID: " + postId));
    }

    public Posts createPost(Posts post, String token) {
        return postsRepository.save(post);
    }

    public Posts updatePost(int postId, Posts updatedPost, String token) {
        Posts post = postsRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Can't Find ID: " + postId));
        post.setContent(updatedPost.getContent());
        return postsRepository.save(post);
    }

    public void deletePost(int postId, String token) {
        postsRepository.deleteById(postId);
    }
}
