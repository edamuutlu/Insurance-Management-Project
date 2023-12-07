package com.insurance.mgmt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "COMPANY")
public class Company {
	@Id
	@Column(name ="company_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int companyId;
	
	@Column(name ="name") 
	private String name;
	
	@Column(name ="country") 
	private String country;
	
	@Column(name ="city") 
	private String city;
	
	@Column(name ="phone_number") 
	private String phoneNumber;
	
	@Column(name ="email") 
	private String email;
	
	@Column(name ="service_fee") 
	private int serviceFee;

	public Company() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Company(int companyId, String name, String country, String city, String phoneNumber, String email,
			int serviceFee) {
		super();
		this.companyId = companyId;
		this.name = name;
		this.country = country;
		this.city = city;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.serviceFee = serviceFee;
	}


	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public int getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(int serviceFee) {
		this.serviceFee = serviceFee;
	}

	@Override
	public String toString() {
		return "Company [companyId=" + companyId + ", name=" + name + ", country=" + country + ", city=" + city
				+ ", phoneNumber=" + phoneNumber + ", email=" + email + ", serviceFee=" + serviceFee + "]";
	}

}
