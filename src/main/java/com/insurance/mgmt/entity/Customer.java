package com.insurance.mgmt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CUSTOMER")
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int customer_id;
	private String tc;
	private String birth;
	private String email;
	private String firstname;
	private String lastname;
	private String province;
	private String district;
	
	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
	public Customer(int customer_id) {
		super();
		this.customer_id = customer_id;
	}	

	public Customer(int customer_id, String tc, String birth, String email, String firstname, String lastname, String province,
			String district) {
		super();
		this.customer_id = customer_id;
		this.tc = tc;
		this.birth = birth;
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.province = province;
		this.district = district;
	}		
	
	public int getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	public String getTc() {
		return tc;
	}

	public void setTc(String tc) {
		this.tc = tc;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}


//	@OneToMany(targetEntity = Customer.class, cascade = CascadeType.ALL)
//	@JoinColumn(name = "customer_id", referencedColumnName = "customer_id", nullable = true)
//	private List<Car> cars;
}
