package com.smart.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.*;
@Entity
@Table(name="USER")
public class User {
     @Id
     @GeneratedValue(strategy=GenerationType.AUTO)
     private int id;
     private String role;
     @NotBlank(message="can't put name blank")
     @Size(min=2,max=20,message="Enter name greater than 2 and less than 20 character")
     private String name;
     @NotBlank(message="can't put email blank")
     @Column(unique=true)
     private String email;
     @NotBlank(message="can't put password blank")
    // @Size(min=3,max=8,message="please enter password in the range 3-8")
	 private String password;
     private String imageurl;
     private boolean enabled;
     @Column(length= 500)
     @NotBlank(message="can't put blank")
     private String about;
     
     @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy="user")
     private List<Contact> list = new ArrayList<>();
     
     public User() {
 		super();
 		// TODO Auto-generated constructor stub
 	}
	public User(int id, String role, String name, String email, String password, String imageurl, boolean enabled,
			String about) {
		super();
		this.id = id;
		this.role = role;
		this.name = name;
		this.email = email;
		this.password = password;
		this.imageurl = imageurl;
		this.enabled = enabled;
		this.about = about;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public List<Contact> getList() {
		return list;
	}
	public void setList(List<Contact> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", role=" + role + ", name=" + name + ", email=" + email + ", password=" + password
				+ ", imageurl=" + imageurl + ", enabled=" + enabled + ", about=" + about + "]";
	}
}
