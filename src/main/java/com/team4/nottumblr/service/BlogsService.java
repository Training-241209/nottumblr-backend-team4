package com.team4.nottumblr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.model.Blogs;
import com.team4.nottumblr.repository.BlogsRepository;

@Service
public class BlogsService {
    
    @Autowired
    private BlogsRepository blogsRepository;

    public List<Blogs> getAllBlogs(String token) {
        return blogsRepository.findAll();
    }
    
    public Blogs createBlog(Blogs blog, String token) {
        if (blog.getTitle() == null || blog.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Blog title cannot be empty.");
        }
        if (blog.getDescription() == null || blog.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Blog description cannot be empty.");
        }
        return blogsRepository.save(blog);
    }

    public Blogs updateBlog(int blogId, Blogs updatedBlog, String token) {
        Blogs existingBlog = blogsRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog with ID " + blogId + " not found."));

        if (updatedBlog.getTitle() != null && !updatedBlog.getTitle().isEmpty()) {
            existingBlog.setTitle(updatedBlog.getTitle());
        }
        if (updatedBlog.getDescription() != null && !updatedBlog.getDescription().isEmpty()) {
            existingBlog.setDescription(updatedBlog.getDescription());
        }
        return blogsRepository.save(existingBlog);
    }

    public void deleteBlog(int blogId, String token) {
        if (!blogsRepository.existsById(blogId)) {
            throw new IllegalArgumentException("Blog with ID " + blogId + " does not exist.");
        }
        blogsRepository.deleteById(blogId);
    }

    public Blogs getBlogById(int blogId, String token) {
        return blogsRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog with ID " + blogId + " not found."));
    }
}
