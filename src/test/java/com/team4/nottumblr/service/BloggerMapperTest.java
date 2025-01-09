package com.team4.nottumblr.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.team4.nottumblr.dto.BloggersDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Roles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BloggerMapperTest {

    private BloggerMapper bloggerMapper;

    @BeforeEach
    void setUp() {
        bloggerMapper = new BloggerMapper(); 
    }

    @Test
    void testConvertToBloggersDTO() {
        // Arrange
        Bloggers blogger = new Bloggers();
        blogger.setBloggerId(1L);
        blogger.setUsername("testuser");
        blogger.setRole(new Roles("ADMIN")); 

        // Act
        BloggersDTO bloggersDTO = bloggerMapper.convertToBloggersDTO(blogger);

        // Assert
        assertThat(bloggersDTO).isNotNull();
        assertThat(bloggersDTO.getBloggerId()).isEqualTo(1L);
        assertThat(bloggersDTO.getUsername()).isEqualTo("testuser");
        assertThat(bloggersDTO.getRoleName()).isEqualTo("ADMIN");
    }
}
