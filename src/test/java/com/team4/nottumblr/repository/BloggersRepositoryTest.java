// package com.team4.nottumblr.repository;

// import static org.assertj.core.api.Assertions.assertThat;

// import com.team4.nottumblr.model.Bloggers;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

// @DataJpaTest // Configures an in-memory database for testing
// public class BloggersRepositoryTest {

//     @Autowired
//     private BloggersRepositoryTest bloggersRepositoryTest;

//     @Test
//     void testSaveAndFindById() {
//         // Arrange
//         Bloggers blogger = new Bloggers();
//         blogger.setUsername("TestUser");
//         blogger.setEmail("test@example.com");

//         // Act
//         Bloggers savedBlogger = bloggersRepositoryTest.save(blogger);
//         Bloggers foundBlogger = bloggersRepositoryTest.findByBloggerId(savedBlogger.getBloggerId()).orElse(null);

//         // Assert
//         assertThat(foundBlogger).isNotNull();
//         assertThat(foundBlogger.getUsername()).isEqualTo("TestUser");
//         assertThat(foundBlogger.getEmail()).isEqualTo("test@example.com");
//     }

//     @Test
//     void testExistsByUsername() {
//         // Arrange
//         Bloggers blogger = new Bloggers();
//         blogger.setUsername("ExistingUser");
//         bloggersRepositoryTest.save(blogger);

//         // Act
//         boolean exists = bloggersRepositoryTest.existsByUsername("ExistingUser");

//         // Assert
//         assertThat(exists).isTrue();
//     }

//     @Test
//     void testExistsByEmail() {
//         // Arrange
//         Bloggers blogger = new Bloggers();
//         blogger.setEmail("existing@example.com");
//         bloggersRepositoryTest.save(blogger);

//         // Act
//         boolean exists = bloggersRepositoryTest.existsByEmail("existing@example.com");

//         // Assert
//         assertThat(exists).isTrue();
//     }

//     @Test
//     void testFindByUsername() {
//         // Arrange
//         Bloggers blogger = new Bloggers();
//         blogger.setUsername("FindMe");
//         bloggersRepositoryTest.save(blogger);

//         // Act
//         Bloggers foundBlogger = bloggersRepositoryTest.findByUsername("FindMe");

//         // Assert
//         assertThat(foundBlogger).isNotNull();
//         assertThat(foundBlogger.getUsername()).isEqualTo("FindMe");
//     }
// }
