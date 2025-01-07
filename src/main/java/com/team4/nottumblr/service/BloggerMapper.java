package com.team4.nottumblr.service;

import org.springframework.stereotype.Component;

import com.team4.nottumblr.dto.BloggersDTO;
import com.team4.nottumblr.model.Bloggers;

@Component
public class BloggerMapper {
    
    public BloggersDTO convertToBloggersDTO(Bloggers blogger) {
        BloggersDTO bloggersDTO = new BloggersDTO();
        bloggersDTO.setBlogger_id(blogger.getBlogger_id());
        bloggersDTO.setUsername(blogger.getUsername());
        return bloggersDTO;
    }
}
