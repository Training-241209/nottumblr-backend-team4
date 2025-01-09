package com.team4.nottumblr.exception;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.team4.nottumblr.controller.AuthController;
import com.team4.nottumblr.dto.JwtResponseDTO;
import com.team4.nottumblr.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = AuthController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void testHandleUnauthorized() throws Exception {
        // Arrange
        when(authService.authenticateBlogger("invaliduser", "wrongpassword"))
                .thenThrow(new IllegalArgumentException("Unauthorized access"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType("application/json")
                .content("""
                        {
                            "username": "invaliduser",
                            "password": "wrongpassword"
                        }
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized access"));
    }

    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        JwtResponseDTO responseDTO = new JwtResponseDTO("validToken");
        when(authService.authenticateBlogger("validuser", "password123"))
                .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType("application/json")
                .content("""
                        {
                            "username": "validuser",
                            "password": "password123"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("validToken"));
    }
}
