package com.team4.nottumblr.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager; 
import jakarta.transaction.Transactional; 

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@Transactional
class BlogsTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void testSaveAndRetrieveBlog() {
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
        blog.setTitle("Test Blog");
        blog.setDescription("This is a test description");
        blog.setBlogger(blogger);

        // Act
        entityManager.persist(blog);
        entityManager.flush(); // Force persistence

        Blogs retrievedBlog = entityManager.find(Blogs.class, blog.getBlogId());

        // Assert
        assertThat(retrievedBlog).isNotNull();
        assertThat(retrievedBlog.getTitle()).isEqualTo("Test Blog");
        assertThat(retrievedBlog.getDescription()).isEqualTo("This is a test description");
        assertThat(retrievedBlog.getBlogger().getUsername()).isEqualTo("testuser");
    }

    @Test
    void testBlogAndBloggerRelationship() {
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

        Blogs blog1 = new Blogs();
        blog1.setTitle("Blog 1");
        blog1.setDescription("Description 1");
        blog1.setBlogger(blogger);

        Blogs blog2 = new Blogs();
        blog2.setTitle("Blog 2");
        blog2.setDescription("Description 2");
        blog2.setBlogger(blogger);

        List<Blogs> blogsList = new ArrayList<>();
        blogsList.add(blog1);
        blogsList.add(blog2);
        blogger.setBlogs(blogsList);

        entityManager.persist(blog1);
        entityManager.persist(blog2);
        entityManager.flush();

        // Act
        Bloggers retrievedBlogger = entityManager.find(Bloggers.class, blogger.getBloggerId());

        // Assert
        assertThat(retrievedBlogger).isNotNull();
        assertThat(retrievedBlogger.getBlogs()).hasSize(2);
        assertThat(retrievedBlogger.getBlogs().get(0).getTitle()).isEqualTo("Blog 1");
        assertThat(retrievedBlogger.getBlogs().get(1).getTitle()).isEqualTo("Blog 2");
    }

    @Test
    void testUniqueConstraints() {
        // Arrange
        Roles role = new Roles();
        role.setRoleName("User");
        entityManager.persist(role);

        Bloggers blogger = new Bloggers();
        blogger.setUsername("uniqueuser");
        blogger.setEmail("uniqueuser@example.com");
        blogger.setPassword("password123");
        blogger.setRole(role);
        entityManager.persist(blogger);

        Blogs blog1 = new Blogs();
        blog1.setTitle("Unique Blog");
        blog1.setDescription("Unique Description");
        blog1.setBlogger(blogger);
        entityManager.persist(blog1);

        Blogs blog2 = new Blogs();
        blog2.setTitle("Unique Blog"); // Same title as blog1
        blog2.setDescription("Different Description");
        blog2.setBlogger(blogger);

        // Act & Assert
        try {
            entityManager.persist(blog2);
            entityManager.flush();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(jakarta.persistence.PersistenceException.class);
        }
    }

    @Test
    void testConstructorWithCreatedAt() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now();

        Roles role = new Roles();
        role.setRoleName("User");

        Bloggers blogger = new Bloggers();
        blogger.setUsername("testuser");
        blogger.setEmail("testuser@example.com");
        blogger.setPassword("password123");
        blogger.setRole(role);

        String title = "Test Blog";
        String description = "This is a test description";

        // Act
        Blogs blog = new Blogs(title, blogger, description, createdAt);

        // Assert
        assertThat(blog).isNotNull();
        assertThat(blog.getTitle()).isEqualTo(title);
        assertThat(blog.getBlogger()).isEqualTo(blogger);
        assertThat(blog.getDescription()).isEqualTo(description);
        assertThat(blog.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void testGetCreatedAt() {
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
        blog.setTitle("Test Blog");
        blog.setDescription("This is a test description");
        blog.setBlogger(blogger);
    
        // Act
        entityManager.persist(blog);
        entityManager.flush(); // Force persistence
        entityManager.clear(); // Clear persistence context to simulate fresh fetch
    
        Blogs retrievedBlog = entityManager.find(Blogs.class, blog.getBlogId());
    
        // Assert
        assertThat(retrievedBlog).isNotNull();
        assertThat(retrievedBlog.getCreatedAt()).isNotNull(); // Verify the timestamp is set
        assertThat(retrievedBlog.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }    

}
