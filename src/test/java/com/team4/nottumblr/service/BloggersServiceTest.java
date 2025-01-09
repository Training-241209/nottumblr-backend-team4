package com.team4.nottumblr.service;

import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Blogs;
import com.team4.nottumblr.repository.BloggersRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BloggersServiceTest {

    @Mock
    private BloggersRepository bloggersRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private BloggersService bloggersService;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        org.mockito.MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExistsBloggerByBloggerId() {
        // Arrange
        long bloggerId = 1L;
        when(bloggersRepository.existsById(bloggerId)).thenReturn(true);

        // Act
        boolean result = bloggersService.existsBloggerByBloggerId(bloggerId);

        // Assert
        assertThat(result).isTrue();
        verify(bloggersRepository, times(1)).existsById(bloggerId);
    }

    @Test
    void testGetBloggerById_ValidId() {
        // Arrange
        long bloggerId = 1L;
    
        Bloggers blogger = new Bloggers();
        blogger.setBloggerId(bloggerId);
    
        // Mock behavior
        when(bloggersRepository.existsById(bloggerId)).thenReturn(true); // Mock existsById to return true
        when(bloggersRepository.findById(bloggerId)).thenReturn(Optional.of(blogger)); // Mock findById to return blogger
    
        // Act
        Bloggers result = bloggersService.getBloggerById(bloggerId);
    
        // Assert
        assertThat(result).isNotNull(); // Ensure result is not null
        assertThat(result.getBloggerId()).isEqualTo(bloggerId); // Check the ID matches
        verify(bloggersRepository, times(1)).existsById(bloggerId); // Verify existsById is called once
        verify(bloggersRepository, times(1)).findById(bloggerId);
    }

    @Test
    void testGetBloggerById_InvalidId() {
        // Arrange
        long bloggerId = 2L;
        when(bloggersRepository.findById(bloggerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bloggersService.getBloggerById(bloggerId));
        verify(bloggersRepository, times(1)).existsById(bloggerId);
    }


    @Test
    void testGetAllBlogsByBlogger_ValidId() {
        // Arrange
        long bloggerId = 1L;
        Bloggers blogger = new Bloggers();
        List<Blogs> blogsList = new ArrayList<>();
        blogsList.add(new Blogs()); // Assuming Blogs is a class
        blogger.setBlogs(blogsList);

        when(bloggersRepository.findById(bloggerId)).thenReturn(Optional.of(blogger));

        // Act
        List<Blogs> result = bloggersService.getAllBlogsByBlogger(bloggerId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        verify(bloggersRepository, times(1)).findById(bloggerId);
    }

    @Test
    void testGetAllBlogsByBlogger_InvalidId() {
        // Arrange
        long bloggerId = 2L;
        when(bloggersRepository.findById(bloggerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bloggersService.getAllBlogsByBlogger(bloggerId));
        verify(bloggersRepository, times(1)).findById(bloggerId);
    }

}
