package com.team4.nottumblr.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Roles;
import com.team4.nottumblr.repository.RolesRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private RolesRepository rolesRepository;

    public String generateToken(Bloggers blogger) {
        return Jwts.builder()
                .claim("id", blogger.getBloggerId())
                .claim("email", blogger.getEmail())
                .claim("role",blogger.getRole().getRoleName())
                .claim("username", blogger.getUsername())
                .claim("firstName", blogger.getFirstName())
                .claim("lastName", blogger.getLastName())
                // Add other fields
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 minutes
                .signWith(getSigningKey())
                                .compact();
                    }

    protected SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }              

    public Bloggers decodeToken(String token) {
        var claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        String roleName = claims.get("role", String.class);
        Roles role = rolesRepository.findByRoleName(roleName);
        Long userId = claims.get("id", Long.class);
        String email = claims.get("email", String.class);
        String username = claims.get("username", String.class);
        String firstName = claims.get("firstName", String.class);
        String lastName = claims.get("lastName", String.class);

        return new Bloggers(userId, email, role, username, firstName, lastName);
    }

    public boolean isCloseToExpiry(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date(System.currentTimeMillis() + 1000 * 60 * 5));
    }


    public boolean isValidToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .after(new Date(System.currentTimeMillis()));
    }

}
