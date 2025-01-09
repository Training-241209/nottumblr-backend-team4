package com.team4.nottumblr.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@Transactional
class BloggersTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void testSaveAndRetrieveBlogger() {
        // Arrange
        Roles role = new Roles();
        role.setRoleName("User");
        entityManager.persist(role);

        Bloggers blogger = new Bloggers();
        blogger.setUsername("testuser");
        blogger.setEmail("testuser@example.com");
        blogger.setPassword("password123");
        blogger.setRole(role);
        blogger.setProfilePictureUrl("http://example.com/profile.jpg");
        blogger.setCreatedAt(LocalDateTime.now());

        // Act
        entityManager.persist(blogger);
        entityManager.flush(); // Force persistence

        Bloggers retrievedBlogger = entityManager.find(Bloggers.class, blogger.getBloggerId());

        // Assert
        assertThat(retrievedBlogger).isNotNull();
        assertThat(retrievedBlogger.getUsername()).isEqualTo("testuser");
        assertThat(retrievedBlogger.getEmail()).isEqualTo("testuser@example.com");
        assertThat(retrievedBlogger.getRole().getRoleName()).isEqualTo("User");
        assertThat(retrievedBlogger.getProfilePictureUrl()).isEqualTo("http://example.com/profile.jpg");
    }

    @Test
    void testDatabaseConstraintViolationForNullPassword() {
        // Arrange
        Roles role = new Roles();
        role.setRoleName("User");
        entityManager.persist(role);

        Bloggers blogger = new Bloggers();
        blogger.setUsername("testuser");
        blogger.setEmail("testuser@example.com");
        blogger.setRole(role);
        // Password is not set, violating the `NOT NULL` constraint

        // Act & Assert
        assertThatThrownBy(() -> {
            entityManager.persist(blogger);
            entityManager.flush(); // Force persistence to database
        }).isInstanceOf(org.hibernate.exception.ConstraintViolationException.class)
          .hasMessageContaining("NULL not allowed for column \"PASSWORD\"");
    }

    @Test
    void testBloggerWithBlogs() {
        // Arrange
        Roles role = new Roles();
        role.setRoleName("User");
        entityManager.persist(role);
    
        Bloggers blogger = new Bloggers();
        blogger.setUsername("testuser");
        blogger.setEmail("testuser@example.com");
        blogger.setPassword("password123");
        blogger.setRole(role);
        entityManager.persist(blogger);
    
        Blogs blog = new Blogs();
        blog.setTitle("First Blog");
        blog.setDescription("This is a test description");
        blog.setBlogger(blogger);
        entityManager.persist(blog);
    
        entityManager.flush();
        entityManager.clear(); // Clear persistence context to simulate fresh fetch
    
        // Act
        Bloggers retrievedBlogger = entityManager.find(Bloggers.class, blogger.getBloggerId());
        retrievedBlogger.getBlogs().size(); // Initialize lazy-loaded blogs
    
        // Assert
        assertThat(retrievedBlogger).isNotNull();
        assertThat(retrievedBlogger.getBlogs()).hasSize(1);
        assertThat(retrievedBlogger.getBlogs().get(0).getTitle()).isEqualTo("First Blog");
    }

    @Test
    void testConstructorWithCreatedAt() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now();
        Roles role = new Roles();
        role.setRoleName("User");

        // Act
        Bloggers blogger = new Bloggers(
                1L, 
                role, 
                "testuser", 
                "testuser@example.com", 
                "password123", 
                "http://example.com/profile.jpg", 
                createdAt
        );

        // Assert
        assertThat(blogger).isNotNull();
        assertThat(blogger.getBloggerId()).isEqualTo(1L);
        assertThat(blogger.getRole()).isEqualTo(role);
        assertThat(blogger.getUsername()).isEqualTo("testuser");
        assertThat(blogger.getEmail()).isEqualTo("testuser@example.com");
        assertThat(blogger.getPassword()).isEqualTo("password123");
        assertThat(blogger.getProfilePictureUrl()).isEqualTo("http://example.com/profile.jpg");
        assertThat(blogger.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void testGetCreatedAt() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now();
        Bloggers blogger = new Bloggers();
        blogger.setCreatedAt(createdAt);

        // Act
        LocalDateTime retrievedCreatedAt = blogger.getCreatedAt();

        // Assert
        assertThat(retrievedCreatedAt).isNotNull();
        assertThat(retrievedCreatedAt).isEqualTo(createdAt);
    }
}
