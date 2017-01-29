package com.example.Models;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "\"user\"")
public class User {
	/*
	 *   userid integer NOT NULL DEFAULT nextval('user_userid_seq'::regclass),
  		username text NOT NULL,
  		password text NOT NULL,
  		CONSTRAINT userid PRIMARY KEY (userid)
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "\"userid\"")
	private Integer userID;
	
	@NotNull
	@Column(name = "\"username\"", unique=true)
	private String username;
	
	@NotNull
	@Column(name = "\"password\"")
	private String password;

	@ElementCollection(targetClass = Role.class)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "userid"))
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private List<Role>roles;
	
	
	public User(){
		
	}
	
	public User(int userid) { 
	    this.userID = userid;
	}
	
	public User(int userid, String username, String password, List<Role>roles){
		this.userID = userid;
		this.username = username;
		this.password = password;
		this.roles = roles;
	}
	
	public long getUserid() {
		return userID;
	}

	public void setUserid(int userid) {
		this.userID = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public List<Role>getRoles(){
		return this.roles;
	}
	
	public void setRoles(List<Role> roles){
		this.roles = roles;
	}
	
	private String getAllRoles(){
		String roles = "";
		for(int i = 0; i < this.roles.size(); i++){
			roles += this.roles.get(i);
			if(i < this.roles.size() - 1){
				roles += ", ";
			}
		}
		return roles;
	}
	
	@Override
	public String toString(){
		String lineBreak = System.getProperty("line.separator");
		
		return "UserID: " + this.userID + lineBreak +
				"Username: " + this.username + lineBreak +
				"Password: " + this.password + lineBreak + 
				"Roles : " + this.getAllRoles();
	}
}


