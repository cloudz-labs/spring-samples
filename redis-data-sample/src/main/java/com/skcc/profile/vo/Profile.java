package com.skcc.profile.vo;

import java.io.Serializable;

public class Profile implements Serializable {

	private static final long serialVersionUID = 6203002013136594235L;

	private String username;
	
	private String id;
	
	private String name;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Profile [username:"+ username +", id:"+ id +", name:"+ name +"]";
	}
	
}
