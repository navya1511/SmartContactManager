package com.smart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="CONTACT")
public class Contact {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int cID;
	
	private String name;
	private String work;
	private String secondName;
	private String email;
	private String phone;
	private String image;
	@Column(length=1000)
	private String description;
	
	@ManyToOne
	private User user;
	
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Contact(int cID, String name, String work, String secondName, String email, String phone, String image,
			String description) {
		super();
		this.cID = cID;
		this.name = name;
		this.work = work;
		this.secondName = secondName;
		this.email = email;
		this.phone = phone;
		this.image = image;
		this.description = description;
	}
	public int getcID() {
		return cID;
	}
	public void setcID(int cID) {
		this.cID = cID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
//	@Override
//	public String toString() {
//		return "Contact [cID=" + cID + ", name=" + name + ", work=" + work + ", secondName=" + secondName + ", email="
//				+ email + ", phone=" + phone + ", image=" + image + ", description=" + description + "]";
//	}
	
	
	
	

}
