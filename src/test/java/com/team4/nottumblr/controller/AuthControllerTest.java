package com.team4.nottumblr.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.team4.nottumblr.dto.BloggersDTO;
import com.team4.nottumblr.dto.JwtResponseDTO;
import com.team4.nottumblr.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import jakarta.servlet.http.Cookie;

@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        String username = "testuser";
        String password = "password123";
        JwtResponseDTO responseDTO = new JwtResponseDTO("validToken");

        when(authService.authenticateBlogger(username, password)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "testuser",
                            "password": "password123"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("validToken"));
    }

    @Test
    void testLogin_Unauthorized() throws Exception {
        // Arrange
        String username = "wronguser";
        String password = "wrongpassword";

        doThrow(new IllegalArgumentException("Invalid username or password"))
                .when(authService)
                .authenticateBlogger(username, password);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "wronguser",
                            "password": "wrongpassword"
                        }
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    void testRegister_Success() throws Exception {
        // Arrange
        doNothing().when(authService).registerBlogger(any());

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "newuser",
                            "email": "newuser@example.com",
                            "password": "password123"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(content().string("Blogger registered successfully."));
    }

    @Test
    void testRegister_Conflict() throws Exception {
        // Arrange
        doThrow(new IllegalArgumentException("Username already exists"))
                .when(authService)
                .registerBlogger(any());

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "existinguser",
                            "email": "existing@example.com",
                            "password": "password123"
                        }
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Username already exists"));
    }

    @Test
    void testLogout_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully."));
    }

    @Test
    void testGetCurrentUser_Success() throws Exception {
        // Arrange
        String token = "validToken";
        BloggersDTO bloggerDTO = new BloggersDTO();
        bloggerDTO.setBloggerId(1L);
        bloggerDTO.setUsername("testuser");
        bloggerDTO.setRoleName("User");
    
        when(authService.getBlogger(token)).thenReturn(bloggerDTO);
    
        // Act & Assert
        mockMvc.perform(get("/auth/me")
                .cookie(new Cookie("jwt", token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bloggerId").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.roleName").value("User"));
    }
    

    @Test
    void testGetCurrentUser_Unauthorized() throws Exception {
        // Arrange
        doThrow(new IllegalArgumentException("Unauthorized access"))
                .when(authService)
                .getBlogger(null);

        // Act & Assert
        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized access"));
    }
}
