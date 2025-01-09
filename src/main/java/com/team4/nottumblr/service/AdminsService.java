package com.team4.nottumblr.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.dto.BloggersDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Roles;
import com.team4.nottumblr.repository.BloggersRepository;

@Service
public class AdminsService {

    @Autowired
    private BloggersRepository bloggersRepository;

    @Autowired
    private BloggersService bloggersService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BloggerMapper bloggerMapper;

    public List<BloggersDTO> getAllBloggers(String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        if (currentBlogger.getRole().getRoleName().equals("ADMIN")) {
            List<Bloggers> bloggers = bloggersRepository.findAll();
            return bloggers.stream()
                    .map(blogger -> bloggerMapper.convertToBloggersDTO(blogger))
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Only admins can view all bloggers");
        }
    }

    public Bloggers promoteBloggerById(long bloggerId, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        if (currentBlogger.getRole().getRoleName().equals("ADMIN")) {
            if (!bloggersRepository.existsById(bloggerId)) {
                throw new IllegalArgumentException("Blogger with ID: " + bloggerId + " does not exist.");
            }

            Bloggers bloggerToPromote = bloggersRepository.findById(bloggerId).orElse(null);

            Roles newRole = new Roles("ADMIN");
            bloggerToPromote.setRole(newRole);
            return bloggersRepository.save(bloggerToPromote);
        } else {
            throw new IllegalArgumentException("Only admins can promote bloggers");
        }
    }

    public void deleteBloggerById(long bloggerId, String token) {
        Bloggers blogger = bloggersService.getBloggerById(bloggerId);
        if (blogger.getUsername().equals(token)) { // users can delete their own
            bloggersRepository.delete(blogger);
        }

        if (blogger.getRole().getRoleName().equals("ADMIN")) { // admins can delete a user
            bloggersRepository.delete(blogger);
        }
    }

    
}
