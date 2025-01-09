package com.team4.nottumblr.service;

import org.springframework.stereotype.Component;

import com.team4.nottumblr.dto.BloggersDTO;
import com.team4.nottumblr.model.Bloggers;

@Component
public class BloggerMapper {
    
    public BloggersDTO convertToBloggersDTO(Bloggers blogger) {
        BloggersDTO bloggersDTO = new BloggersDTO();
        bloggersDTO.setBloggerId(blogger.getBloggerId());
        bloggersDTO.setUsername(blogger.getUsername());
        bloggersDTO.setRoleName(blogger.getRole().getRoleName());
        return bloggersDTO;
    }
}
