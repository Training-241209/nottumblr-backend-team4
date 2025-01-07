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

    public Blogs updateBlog(int blog_id, Blogs updatedBlog, String token) {
        Blogs existingBlog = blogsRepository.findById(blog_id)
                .orElseThrow(() -> new IllegalArgumentException("Blog with ID " + blog_id + " not found."));

        if (updatedBlog.getTitle() != null && !updatedBlog.getTitle().isEmpty()) {
            existingBlog.setTitle(updatedBlog.getTitle());
        }
        if (updatedBlog.getDescription() != null && !updatedBlog.getDescription().isEmpty()) {
            existingBlog.setDescription(updatedBlog.getDescription());
        }
        return blogsRepository.save(existingBlog);
    }

    public void deleteBlog(int blog_id, String token) {
        if (!blogsRepository.existsById(blog_id)) {
            throw new IllegalArgumentException("Blog with ID " + blog_id + " does not exist.");
        }
        blogsRepository.deleteById(blog_id);
    }

    public Blogs getBlogById(int blog_id, String token) {
        return blogsRepository.findById(blog_id)
                .orElseThrow(() -> new IllegalArgumentException("Blog with ID " + blog_id + " not found."));
    }
}
