package com.team4.nottumblr.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.team4.nottumblr.dto.BloggersDTO;
import com.team4.nottumblr.dto.JwtResponseDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Roles;
import com.team4.nottumblr.repository.BloggersRepository;
import com.team4.nottumblr.repository.RolesRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

class AuthServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private BloggersRepository bloggersRepository;

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BloggerMapper bloggerMapper;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        org.mockito.MockitoAnnotations.openMocks(this);
    }

    // Test for registerBlogger
    @Test
    void testRegisterBlogger_Success() {
        // Arrange
        Bloggers blogger = new Bloggers();
        blogger.setUsername("newuser");
        blogger.setEmail("newuser@example.com");
        blogger.setPassword("plaintextpassword");

        when(bloggersRepository.existsByUsername(blogger.getUsername())).thenReturn(false);
        when(bloggersRepository.existsByEmail(blogger.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("plaintextpassword")).thenReturn("hashedpassword");
        when(rolesRepository.findByRoleName("User")).thenReturn(new Roles("User"));

        // Act
        authService.registerBlogger(blogger);

        // Assert
        assertThat(blogger.getPassword()).isEqualTo("hashedpassword");
        assertThat(blogger.getRole().getRoleName()).isEqualTo("User");
        verify(bloggersRepository, times(1)).save(blogger);
    }

    @Test
    void testRegisterBlogger_UsernameExists() {
        // Arrange
        Bloggers blogger = new Bloggers();
        blogger.setUsername("existinguser");

        when(bloggersRepository.existsByUsername(blogger.getUsername())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.registerBlogger(blogger));
    }

    @Test
    void testRegisterBlogger_EmailExists() {
        // Arrange
        Bloggers blogger = new Bloggers();
        blogger.setEmail("existingemail@example.com");

        when(bloggersRepository.existsByEmail(blogger.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.registerBlogger(blogger));
    }

    // Test for authenticateBlogger
    @Test
    void testAuthenticateBlogger_Success() {
        // Arrange
        Bloggers blogger = new Bloggers();
        blogger.setUsername("validuser");
        blogger.setPassword("hashedpassword");

        when(bloggersRepository.findByUsername("validuser")).thenReturn(blogger);
        when(passwordEncoder.matches("plaintextpassword", "hashedpassword")).thenReturn(true);
        when(jwtService.generateToken(blogger)).thenReturn("validToken");

        // Act
        JwtResponseDTO response = authService.authenticateBlogger("validuser", "plaintextpassword");

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("validToken");
    }

    @Test
    void testAuthenticateBlogger_InvalidCredentials() {
        // Arrange
        when(bloggersRepository.findByUsername("invaliduser")).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.authenticateBlogger("invaliduser", "wrongpassword"));
    }

    @Test
    void testAuthenticateBlogger_BloggerNotFound() {
        // Arrange
        when(bloggersRepository.findByUsername("nonexistentUser")).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.authenticateBlogger("nonexistentUser", "password123"));
    }

    @Test
    void testAuthenticateBlogger_InvalidPassword() {
        // Arrange
        Bloggers blogger = new Bloggers();
        blogger.setUsername("validUser");
        blogger.setPassword("hashedPassword");

        when(bloggersRepository.findByUsername("validUser")).thenReturn(blogger);
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.authenticateBlogger("validUser", "wrongPassword"));
    }





    // Test for getBlogger
    @Test
    void testGetBlogger_Success() {
        // Arrange
        String token = "validToken";
        Bloggers decodedBlogger = new Bloggers(1L, "user@example.com", new Roles("User"));
        Bloggers storedBlogger = new Bloggers(1L, "user@example.com", new Roles("User"));

        when(jwtService.decodeToken(token)).thenReturn(decodedBlogger);
        when(bloggersRepository.findById(1L)).thenReturn(Optional.of(storedBlogger));
        BloggersDTO bloggersDTO = new BloggersDTO();
        bloggersDTO.setBloggerId(1L);
        bloggersDTO.setUsername("user");
        bloggersDTO.setRoleName("User");
        when(bloggerMapper.convertToBloggersDTO(storedBlogger)).thenReturn(bloggersDTO);

        // Act
        BloggersDTO result = authService.getBlogger(token);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getBloggerId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("user");
        assertThat(result.getRoleName()).isEqualTo("User");
    }

    @Test
    void testGetBlogger_InvalidToken() {
        // Arrange
        String invalidToken = "invalidToken";

        when(jwtService.decodeToken(invalidToken)).thenThrow(new IllegalArgumentException("Invalid token"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.getBlogger(invalidToken));
    }
}
