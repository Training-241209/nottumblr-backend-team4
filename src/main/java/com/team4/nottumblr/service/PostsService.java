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

    public List<Posts> getAllPosts(String token, Integer blog_id, Long blogger_id) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        if(blog_id != null) {
            return postsRepository.findByBlog_Blog_id(blog_id);
        }

        if(blogger_id != null) {
            return postsRepository.findByBlog_Blogger_Blogger_id(blogger_id);
        }

        return postsRepository.findAll();
    }

    public Posts getPostById(int post_id, String token) {
        return postsRepository.findById(post_id).orElseThrow(() -> new IllegalArgumentException("Can't Find ID: " + post_id));
    }

    public Posts createPost(Posts post, String token) {
        return postsRepository.save(post);
    }

    public Posts updatePost(int post_id, Posts updatedPost, String token) {
        Posts post = postsRepository.findById(post_id).orElseThrow(() -> new IllegalArgumentException("Can't Find ID: " + post_id));
        post.setContent(updatedPost.getContent());
        return postsRepository.save(post);
    }

    public void deletePost(int post_id, String token) {
        postsRepository.deleteById(post_id);
    }
}
