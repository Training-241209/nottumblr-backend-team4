package com.team4.nottumblr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.model.Likes;
import com.team4.nottumblr.repository.LikesRepository;

@Service
public class LikesService {
    
    @Autowired
    private LikesRepository likesRepository;

    public List<Likes> getAllLikesByPost(int postId) {
        List<Likes> likes = likesRepository.findByPost_PostId(postId);
        return likes;
    }

    public Likes createLike(int postId, String token) {
        Likes like = new Likes(likesRepository.findByPost_PostId(postId).get(0).getPost(), likesRepository.findByPost_PostId(postId).get(0).getBlogger());
        return likesRepository.save(like);
    }

    public void deleteLike(int postId, String token) {
        likesRepository.deleteByPost_PostId(postId);
    }
}
