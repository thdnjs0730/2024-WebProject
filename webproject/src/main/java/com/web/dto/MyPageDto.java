package com.web.dto;

import java.util.List;

public class MyPageDto {

	private String password;
	private String email;
	private String username;
	private List<AdoptDto> posts;
	private List<String> comments;
	private List<AdoptDto> favorites;
	
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
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
	public List<AdoptDto> getFavorites() {
		return favorites;
	}
	public void setFavorites(List<AdoptDto> favorites) {
		this.favorites = favorites;
	}
	
	
}
