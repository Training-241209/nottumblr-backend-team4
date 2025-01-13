// package com.team4.nottumblr.service;

// import com.team4.nottumblr.dto.BloggersDTO;
// import com.team4.nottumblr.dto.JwtResponseDTO;
// import com.team4.nottumblr.model.Bloggers;
// import com.team4.nottumblr.model.Roles;
// import com.team4.nottumblr.repository.BloggersRepository;
// import com.team4.nottumblr.repository.RolesRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.security.crypto.password.PasswordEncoder;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// class AuthServiceTest {

//     @InjectMocks
//     private AuthService authService;

//     @Mock
//     private BloggersRepository bloggersRepository;

//     @Mock
//     private RolesRepository rolesRepository;

//     @Mock
//     private JwtService jwtService;

//     @Mock
//     private PasswordEncoder passwordEncoder;

//     @Mock
//     private BloggerMapper bloggerMapper;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void testRegisterBlogger_Success() {
//         Bloggers blogger = new Bloggers();
//         blogger.setUsername("testuser");
//         blogger.setEmail("testuser@example.com");
//         blogger.setPassword("password123");

//         when(bloggersRepository.existsByUsername("testuser")).thenReturn(false);
//         when(bloggersRepository.existsByEmail("testuser@example.com")).thenReturn(false);
//         when(rolesRepository.findByRoleName("User")).thenReturn(new Roles("User"));
//         when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");

//         authService.registerBlogger(blogger);

//         verify(bloggersRepository).save(blogger);
//         assertEquals("hashedPassword", blogger.getPassword());
//         assertEquals("User", blogger.getRole().getRoleName());
//     }

//     @Test
//     void testRegisterBlogger_UsernameExists() {
//         when(bloggersRepository.existsByUsername("testuser")).thenReturn(true);

//         Bloggers blogger = new Bloggers();
//         blogger.setUsername("testuser");
//         blogger.setEmail("testuser@example.com");
//         blogger.setPassword("password123");

//         Exception exception = assertThrows(IllegalArgumentException.class, () -> authService.registerBlogger(blogger));
//         assertEquals("Username already exists", exception.getMessage());
//     }

//     @Test
//     void testRegisterBlogger_EmailExists() {
//         when(bloggersRepository.existsByEmail("testuser@example.com")).thenReturn(true);

//         Bloggers blogger = new Bloggers();
//         blogger.setUsername("testuser");
//         blogger.setEmail("testuser@example.com");
//         blogger.setPassword("password123");

//         Exception exception = assertThrows(IllegalArgumentException.class, () -> authService.registerBlogger(blogger));
//         assertEquals("Email already exists", exception.getMessage());

//         verify(bloggersRepository, never()).save(any(Bloggers.class));
//     }

//     @Test
//     void testAuthenticateBlogger_Success() {
//         Bloggers blogger = new Bloggers();
//         blogger.setUsername("testuser");
//         blogger.setPassword("hashedPassword");

//         when(bloggersRepository.findByUsername("testuser")).thenReturn(blogger);
//         when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
//         when(jwtService.generateToken(blogger)).thenReturn("mockJwtToken");

//         JwtResponseDTO response = authService.authenticateBlogger("testuser", "password123");

//         assertEquals("mockJwtToken", response.getToken());
//     }

//     @Test
//     void testAuthenticateBlogger_BloggerNotFound() {
//         when(bloggersRepository.findByUsername("nonexistentUser")).thenReturn(null);

//         Exception exception = assertThrows(IllegalArgumentException.class, () -> authService.authenticateBlogger("nonexistentUser", "password123"));
//         assertEquals("Invalid username or password", exception.getMessage());
//     }

//     @Test
//     void testAuthenticateBlogger_InvalidPassword() {
//         Bloggers blogger = new Bloggers();
//         blogger.setUsername("testuser");
//         blogger.setPassword("hashedPassword");

//         when(bloggersRepository.findByUsername("testuser")).thenReturn(blogger);
//         when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

//         Exception exception = assertThrows(IllegalArgumentException.class, () -> authService.authenticateBlogger("testuser", "wrongPassword"));
//         assertEquals("Invalid username or password", exception.getMessage());
//     }

//     @Test
//     void testGetBlogger_Success() {
//         Bloggers blogger = new Bloggers();
//         blogger.setBloggerId(1);
//         blogger.setUsername("testuser");
//         blogger.setEmail("testuser@example.com");
//         blogger.setRole(new Roles("User"));
//         blogger.setFirstName("John");
//         blogger.setLastName("Doe");

//         BloggersDTO bloggersDTO = new BloggersDTO(1, "testuser", "testuser@example.com", "User", "John", "Doe");

//         when(jwtService.decodeToken("mockToken")).thenReturn(blogger);
//         when(bloggersRepository.findById(1L)).thenReturn(java.util.Optional.of(blogger));
//         when(bloggerMapper.convertToBloggersDTO(blogger)).thenReturn(bloggersDTO);

//         BloggersDTO result = authService.getBlogger("mockToken");

//         assertNotNull(result);
//         assertEquals(1, result.getBloggerId());
//         assertEquals("testuser", result.getUsername());
//         assertEquals("User", result.getRoleName());
//     }

//     @Test
//     void testGetBlogger_NotFound() {
//         Bloggers blogger = new Bloggers();
//         blogger.setBloggerId(1);

//         when(jwtService.decodeToken("mockToken")).thenReturn(blogger);
//         when(bloggersRepository.findById(1L)).thenReturn(java.util.Optional.empty());

//         Exception exception = assertThrows(IllegalArgumentException.class, () -> authService.getBlogger("mockToken"));
//         assertEquals("Blogger not found with ID: 1", exception.getMessage());
//     }
// }
