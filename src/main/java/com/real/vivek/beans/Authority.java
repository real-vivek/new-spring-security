package com.real.vivek.beans;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "customer_authorities")
public class Authority {
	
	@Id
	private int auth_id;
	
	@ManyToOne
	@JoinColumn(name = "id")//Referencing the id field from customer bean
	private Customer customer_id;
	
	private String authorityName;

	public int getId() {
		return auth_id;
	}

	public void setId(int id) {
		this.auth_id = id;
	}

	public Customer getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Customer customer_id) {
		this.customer_id = customer_id;
	}

	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authorityName == null) ? 0 : authorityName.hashCode());
		result = prime * result + ((customer_id == null) ? 0 : customer_id.hashCode());
		result = prime * result + auth_id;
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
		Authority other = (Authority) obj;
		if (authorityName == null) {
			if (other.authorityName != null)
				return false;
		} else if (!authorityName.equals(other.authorityName))
			return false;
		if (customer_id == null) {
			if (other.customer_id != null)
				return false;
		} else if (!customer_id.equals(other.customer_id))
			return false;
		if (auth_id != other.auth_id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Authority [id=" + auth_id + ", customer_id=" + customer_id + ", authorityName=" + authorityName + "]";
	}

}
