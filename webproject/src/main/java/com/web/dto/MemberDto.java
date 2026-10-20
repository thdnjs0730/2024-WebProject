package com.web.dto;

import java.util.ArrayList;
import java.util.List;

public class MemberDto {

    private String id;
    private String password;
    private String email;
    private boolean isAdmin;
    private String nickname;
    private List<AdoptDto> posts = new ArrayList<>();
    private List<String> comments = new ArrayList<>();
    private List<AdoptDto> favorites = new ArrayList<>();
    
    
	public List<AdoptDto> getPosts() {
		return posts;
	}
	public void setPosts(List<AdoptDto> posts) {
		this.posts = posts;
	}
	public List<String> getComments() {
		return comments;
	}
	public void setComments(List<String> comments) {
		this.comments = comments;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


}

