package com.team4.nottumblr.service;

import com.team4.nottumblr.dto.BlogsDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Blogs;
import com.team4.nottumblr.model.Roles;
import com.team4.nottumblr.repository.BloggersRepository;
import com.team4.nottumblr.repository.BlogsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BlogsServiceTest {

    @InjectMocks
    private BlogsService blogsService;

    @Mock
    private BlogsRepository blogsRepository;

    @Mock
    private BloggersRepository bloggersRepository;

    @Mock
    private JwtService jwtService;

    private Bloggers mockBlogger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockBlogger = new Bloggers(1L, new Roles("User"), "testuser", "testuser@example.com", "password", null, null);
    }

    @Test
    void testGetAllBlogs() {
        Blogs blog = new Blogs("Test Blog", mockBlogger, "Description", LocalDateTime.now(), new ArrayList<>());
        when(blogsRepository.findAll()).thenReturn(List.of(blog));

        List<BlogsDTO> blogs = blogsService.getAllBlogs("mockToken");

        assertEquals(1, blogs.size());
        assertEquals("Test Blog", blogs.get(0).getTitle());
        assertEquals("testuser", blogs.get(0).getUsername());
        verify(blogsRepository, times(1)).findAll();
    }

    @Test
    void testGetBlogsByBlogger_Success() {
        Blogs blog = new Blogs("Test Blog", mockBlogger, "Description", LocalDateTime.now(), new ArrayList<>());
        when(bloggersRepository.findById(1L)).thenReturn(Optional.of(mockBlogger));
        when(blogsRepository.findByBlogger(mockBlogger)).thenReturn(List.of(blog));

        List<BlogsDTO> blogs = blogsService.getBlogsByBlogger(1L);

        assertEquals(1, blogs.size());
        assertEquals("Test Blog", blogs.get(0).getTitle());
        assertEquals("testuser", blogs.get(0).getUsername());
        verify(bloggersRepository, times(1)).findById(1L);
        verify(blogsRepository, times(1)).findByBlogger(mockBlogger);
    }

    @Test
    void testGetBlogsByBlogger_BloggerNotFound() {
        when(bloggersRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> blogsService.getBlogsByBlogger(1L));
        assertEquals("Blogger with ID '1' not found.", exception.getMessage());

        verify(bloggersRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateBlog_Success() {
        BlogsDTO blogsDTO = new BlogsDTO("Test Blog", "Description", "testuser");
        when(jwtService.decodeToken("mockToken")).thenReturn(mockBlogger);
        when(bloggersRepository.findById(1L)).thenReturn(Optional.of(mockBlogger));

        BlogsDTO createdBlog = blogsService.createBlog(blogsDTO, "mockToken");

        assertEquals("Test Blog", createdBlog.getTitle());
        assertEquals("testuser", createdBlog.getUsername());
        verify(blogsRepository, times(1)).save(any(Blogs.class));
    }


    @Test
    void testUpdateBlog_Success() {
        Blogs existingBlog = new Blogs("Old Title", mockBlogger, "Old Description", LocalDateTime.now(), new ArrayList<>());
        BlogsDTO updatedBlog = new BlogsDTO("New Title", "New Description", "testuser");
        when(jwtService.decodeToken("mockToken")).thenReturn(mockBlogger);
        when(blogsRepository.findById(1)).thenReturn(Optional.of(existingBlog));

        // Mock the save method to return the updated blog
        when(blogsRepository.save(existingBlog)).thenAnswer(invocation -> invocation.getArgument(0));

        BlogsDTO result = blogsService.updateBlog(1, updatedBlog, "mockToken");

        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        verify(blogsRepository, times(1)).save(existingBlog);
    }


    @Test
    void testUpdateBlog_NotAuthorized() {
        Blogs existingBlog = new Blogs("Old Title", new Bloggers(2L, new Roles("User"), "otheruser", "other@example.com", "password", null, null), "Old Description", LocalDateTime.now(), new ArrayList<>());
        BlogsDTO updatedBlog = new BlogsDTO("New Title", "New Description", "testuser");
        when(jwtService.decodeToken("mockToken")).thenReturn(mockBlogger);
        when(blogsRepository.findById(1)).thenReturn(Optional.of(existingBlog));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> blogsService.updateBlog(1, updatedBlog, "mockToken"));
        assertEquals("You do not have permission to update this blog.", exception.getMessage());
    }

    @Test
    void testDeleteBlog_Success() {
        Blogs blog = new Blogs("Test Blog", mockBlogger, "Description", LocalDateTime.now(), new ArrayList<>());
        when(jwtService.decodeToken("mockToken")).thenReturn(mockBlogger);
        when(blogsRepository.findById(1)).thenReturn(Optional.of(blog));

        blogsService.deleteBlog(1, "mockToken");

        verify(blogsRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteBlog_NotAuthorized() {
        Blogs blog = new Blogs("Test Blog", new Bloggers(2L, new Roles("User"), "otheruser", "other@example.com", "password", null, null), "Description", LocalDateTime.now(), new ArrayList<>());
        when(jwtService.decodeToken("mockToken")).thenReturn(mockBlogger);
        when(blogsRepository.findById(1)).thenReturn(Optional.of(blog));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> blogsService.deleteBlog(1, "mockToken"));
        assertEquals("You are not authorized to delete this blog.", exception.getMessage());
    }

    @Test
    void testGetBlogById_Success() {
        Blogs blog = new Blogs();
        blog.setBlogId(1);
        blog.setTitle("Test Blog");
        blog.setDescription("Description");
        blog.setBlogger(mockBlogger);

        when(blogsRepository.findById(1)).thenReturn(Optional.of(blog));

        Blogs result = blogsService.getBlogById(1, "mockToken");

        assertNotNull(result);
        assertEquals(1, result.getBlogId());
        assertEquals("Test Blog", result.getTitle());
        verify(blogsRepository, times(1)).findById(1);
    }

    @Test
    void testGetBlogById_NotFound() {
        when(blogsRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> blogsService.getBlogById(1, "mockToken"));
        assertEquals("Blog with ID 1 not found.", exception.getMessage());
        verify(blogsRepository, times(1)).findById(1);
    }

    @Test
    void testCreateBlog_EmptyTitle() {
        BlogsDTO blogsDTO = new BlogsDTO("", "Description", "testuser");
        when(jwtService.decodeToken("mockToken")).thenReturn(mockBlogger);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> blogsService.createBlog(blogsDTO, "mockToken"));
        assertEquals("Blog title cannot be empty.", exception.getMessage());
        verify(blogsRepository, never()).save(any(Blogs.class));
    }

    @Test
    void testCreateBlog_InvalidToken() {
        when(jwtService.decodeToken("mockToken")).thenReturn(null);

        BlogsDTO blogsDTO = new BlogsDTO("Test Blog", "Description", "testuser");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> blogsService.createBlog(blogsDTO, "mockToken"));
        assertEquals("Invalid token. Blogger not found.", exception.getMessage());
        verify(blogsRepository, never()).save(any(Blogs.class));
    }


}
