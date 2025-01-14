package com.team4.nottumblr.dto;

public class ProfilePictureDTO {
    private String profilePictureUrl;
    
    public ProfilePictureDTO() {}
    
    public ProfilePictureDTO(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
    
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
    
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
