package com.team4.nottumblr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.dto.BlogsDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Blogs;
import com.team4.nottumblr.repository.BloggersRepository;
import com.team4.nottumblr.repository.BlogsRepository;

@Service
public class BlogsService {
    
    @Autowired
    private BlogsRepository blogsRepository;

    @Autowired
    private BloggersRepository bloggersRepository;

    @Autowired 
    private JwtService jwtService;


    public List<BlogsDTO> getAllBlogs(String token) {
        return blogsRepository.findAll()
            .stream()
            .map(blog -> new BlogsDTO(
                blog.getTitle(),
                blog.getDescription(),
                blog.getBlogger().getUsername()
            ))
            .toList();
    }

    public List<BlogsDTO> getBlogsByBlogger(Long bloggerId) {
    // Fetch the blogger by username
        Bloggers blogger = bloggersRepository.findById(bloggerId)
            .orElseThrow(() -> new IllegalArgumentException("Blogger with ID '" + bloggerId + "' not found."));

        // Fetch blogs by the blogger and map to DTO
        return blogsRepository.findByBlogger(blogger)
            .stream()
            .map(blog -> new BlogsDTO(
                blog.getTitle(),
                blog.getDescription(),
                blog.getBlogger().getUsername()
            ))
            .toList();
    }
    
    
    public BlogsDTO createBlog(BlogsDTO blogsDTO, String token) {
        // Decode the token to get the current blogger
        Bloggers currentBlogger = jwtService.decodeToken(token);
    
        // Validate input
        if (blogsDTO.getTitle() == null || blogsDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Blog title cannot be empty.");
        }
        if (blogsDTO.getDescription() == null || blogsDTO.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Blog description cannot be empty.");
        }
        if (currentBlogger == null) {
            throw new IllegalArgumentException("Invalid token. Blogger not found.");
        }
    
        // Fetch the full blogger entity by ID
        Bloggers blogger = bloggersRepository.findById(currentBlogger.getBloggerId())
                .orElseThrow(() -> new IllegalArgumentException("Blogger with ID not found."));
    
        // Create and populate the Blogs entity
        Blogs newBlog = new Blogs();
        newBlog.setTitle(blogsDTO.getTitle());
        newBlog.setDescription(blogsDTO.getDescription());
        newBlog.setBlogger(blogger);
    
        // Persist the Blogs entity
        blogsRepository.save(newBlog);
    
        // Return the BlogsDTO with the required details
        return new BlogsDTO(newBlog.getTitle(), newBlog.getDescription(), blogger.getUsername());
    }
    
    public BlogsDTO updateBlog(int blogId, BlogsDTO updatedBlog, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);
    
        Blogs existingBlog = blogsRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog with ID " + blogId + " not found."));
    
        if (existingBlog.getBlogger().getBloggerId() != currentBlogger.getBloggerId() &&
            !currentBlogger.getRole().getRoleName().equalsIgnoreCase("ADMIN")) {
            throw new IllegalArgumentException("You do not have permission to update this blog.");
        }
    
        if (updatedBlog.getTitle() != null && !updatedBlog.getTitle().isEmpty()) {
            existingBlog.setTitle(updatedBlog.getTitle());
        }
        if (updatedBlog.getDescription() != null && !updatedBlog.getDescription().isEmpty()) {
            existingBlog.setDescription(updatedBlog.getDescription());
        }
    
        Blogs savedBlog = blogsRepository.save(existingBlog);
    
        // Convert the saved blog to BlogsDTO
        return new BlogsDTO(
            savedBlog.getTitle(),
            savedBlog.getDescription(),
            savedBlog.getBlogger().getUsername()
        );
    }
    
    

    public void deleteBlog(int blogId, String token) {
        // Decode the token to get the current blogger
        Bloggers currentBlogger = jwtService.decodeToken(token);
    
        // Fetch the blog from the repository
        Blogs blog = blogsRepository.findById(blogId)
            .orElseThrow(() -> new IllegalArgumentException("Blog with ID " + blogId + " does not exist."));
    
        // Check if the current blogger is either the creator of the blog OR an admin
        boolean isCreator = blog.getBlogger().getBloggerId() == currentBlogger.getBloggerId();
        boolean isAdmin = currentBlogger.getRole().getRoleName().equalsIgnoreCase("ADMIN");
    
        // Throw exception if the user is neither the creator nor an admin
        if (!isCreator && !isAdmin) {
            throw new IllegalArgumentException("You are not authorized to delete this blog.");
        }
    
        // If the conditions pass, delete the blog
        blogsRepository.deleteById(blogId);
    }
    

    public Blogs getBlogById(int blogId, String token) {
        return blogsRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog with ID " + blogId + " not found."));
    }

}
