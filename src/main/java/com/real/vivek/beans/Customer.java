package com.real.vivek.beans;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "customer")
public class Customer {

	@Id
	private int id;
	private String email;
	
	//We need pwd from UI to backend so we don't use @JsonIgnore as this will ignore the pwd property if we do so
	//Instead we use below configuration, which tells that we always want this value from UI application to backend but we don't want password details loaded from backend exposed to UI application. We do this because we don't want to send hashString to be passed from backend to UI exposing the hashString over n/w  
	@JsonProperty(access = Access.WRITE_ONLY)
	private String pwd;
	private String role;
	
	//Single customer can have multiple authorities so one to many relationship
	@JsonIgnore	//This field will not be send to inside json response as this is sensitive information
	@OneToMany(mappedBy = "customer_id", fetch = FetchType.EAGER)//single record in customer can be mapped to many records in authorities, mappedBy will contain name of field we have in Authority bean, by using eager type we are telling to load authorities when loading customer itself eagerly
	private Set<Authority> customer_authorities;
	
	public Set<Authority> getAuthorities() {
		return customer_authorities;
	}
	public void setAuthorities(Set<Authority> authorities) {
		this.customer_authorities = authorities;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + id;
		result = prime * result + ((pwd == null) ? 0 : pwd.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id != other.id)
			return false;
		if (pwd == null) {
			if (other.pwd != null)
				return false;
		} else if (!pwd.equals(other.pwd))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Customer [id=" + id + ", email=" + email + ", pwd=" + pwd + ", role=" + role + "]";
	}
	
}
