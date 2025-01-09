package com.team4.nottumblr.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Roles;
import com.team4.nottumblr.repository.RolesRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

class JwtServiceTest {

    @Mock
    private RolesRepository rolesRepository;

    @InjectMocks
    private JwtService jwtService;

    private final String testSecretKey = "f412457eedee2fbdc5bfd33cbdcaab597c2d3ef6a0c2fa3a901e9e2f17568f05"; // Ensure it's at least 256 bits

    @BeforeEach
    void setUp() {
        org.mockito.MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtService, "secretKey", testSecretKey); // Set the secret key
    }

    @Test
    void testGenerateToken() {
        // Arrange
        Roles role = new Roles("ADMIN");
        Bloggers blogger = new Bloggers(1L, "test@example.com", role);

        // Act
        String token = jwtService.generateToken(blogger);

        // Assert
        assertThat(token).isNotNull();
        var claims = Jwts.parserBuilder()
                .setSigningKey(jwtService.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        assertThat(claims.get("id", Long.class)).isEqualTo(1L);
        assertThat(claims.get("email", String.class)).isEqualTo("test@example.com");
        assertThat(claims.get("role", String.class)).isEqualTo("ADMIN");
    }

    @Test
    void testDecodeToken_ValidToken() {
        // Arrange
        Roles role = new Roles("ADMIN");
        Bloggers blogger = new Bloggers(1L, "test@example.com", role);
        when(rolesRepository.findByRoleName("ADMIN")).thenReturn(role);
        String token = jwtService.generateToken(blogger);

        // Act
        Bloggers decodedBlogger = jwtService.decodeToken(token);

        // Assert
        assertThat(decodedBlogger).isNotNull();
        assertThat(decodedBlogger.getBloggerId()).isEqualTo(1L);
        assertThat(decodedBlogger.getEmail()).isEqualTo("test@example.com");
        assertThat(decodedBlogger.getRole().getRoleName()).isEqualTo("ADMIN");
    }

    @Test
    void testDecodeToken_ExpiredToken() {
        // Arrange
        Roles role = new Roles("ADMIN");
        Bloggers blogger = new Bloggers(1L, "test@example.com", role);
        String expiredToken = Jwts.builder()
                .claim("id", blogger.getBloggerId())
                .claim("email", blogger.getEmail())
                .claim("role", blogger.getRole().getRoleName())
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 30)) // 30 minutes ago
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 15)) // Expired 15 minutes ago
                .signWith(jwtService.getSigningKey())
                .compact();

        // Act & Assert
        assertThrows(ExpiredJwtException.class, () -> jwtService.decodeToken(expiredToken));
    }

    @Test
    void testDecodeToken_InvalidToken() {
        // Arrange
        String invalidToken = "invalidTokenString";

        // Act & Assert
        assertThrows(io.jsonwebtoken.MalformedJwtException.class, () -> jwtService.decodeToken(invalidToken));
    }
}
