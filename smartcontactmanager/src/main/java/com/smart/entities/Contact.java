package com.smart.entities;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="CONTACT")
public class Contact {
	  @Id
	  @GeneratedValue(strategy=GenerationType.AUTO)
      private int Cid;
      private String fname;
      private String secname;
      private String work;
      private String email;
      private String phone;
    //  private String image;
      @Column(length = 50000)
      private String descr;
      @Transient // This will ensure it’s not persisted in the database
      private MultipartFile image;

      private String imageName;
      
    @ManyToOne
    private User user;
	public int getCid() {
		return Cid;
	}
	public void setCid(int cid) {
		Cid = cid;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public MultipartFile getImage() {
		return image;
	}
	public void setImage(MultipartFile image) {
		this.image = image;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getSecname() {
		return secname;
	}
	public void setSecname(String secname) {
		this.secname = secname;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/*public String getImage() {
		return image;
	}*/
	/*public void setImage(String image) {
		this.image = image;
	}*/
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Contact [Cid=" + Cid + ", fname=" + fname + ", secname=" + secname + ", work=" + work + ", email=" + email
				+ ", phone=" + phone + ", descr=" + descr + ", user=" + user + "]";
	}
	
      

      
}
