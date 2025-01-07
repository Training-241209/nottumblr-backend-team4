package com.team4.nottumblr.dto;

public class JwtResponseDTO {
    private String token;
    private String type = "Bearer";

    public JwtResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

}