package com.insurance.mgmt.entity;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "CUSTOMER")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int customer_id;

	@NotEmpty(message = "Enter your tc number.")
	@Digits(integer=11, fraction=0, message= "Only numeric input is allowed.")
	@Size(min = 11, max = 11, message = "TC number must be 11 digits.")
	private String tc;

	@NotBlank(message = "Enter your birth.")
	//@Past(message = "Enter a past date")
	@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Enter a valid date in the format yyyy-mm-dd")
	private String birth;

	@NotBlank(message = "Enter your email.")
	@Email(message = "Enter a valid email address.")
	private String email;

	@NotBlank(message = "Enter your first name.")
	@Pattern(regexp = "[a-zA-Z\\s]+", message = "Only alphabetic characters and spaces are allowed")
	@Length(min = 2, message = "Name must be at least 2 characters")
	private String firstname;
	
	@NotBlank(message = "Enter your last name")
	@Pattern(regexp = "[a-zA-Z\\s]+", message = "Only alphabetic characters and spaces are allowed")
	@Length(min = 2, message = "Name must be at least 2 characters")
	private String lastname;
	
	@NotBlank(message = "Enter your province.")
	private String province;
	
	@NotBlank(message = "Enter your district.")
	private String district;

	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Customer(int customer_id, String tc, String birth, String email, String firstname, String lastname,
			String province, String district) {
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

	@Override
	public String toString() {
		return "Customer [customer_id=" + customer_id + ", tc=" + tc + ", birth=" + birth + ", email=" + email
				+ ", firstname=" + firstname + ", lastname=" + lastname + ", province=" + province + ", district="
				+ district + "]";
	}

//	@OneToMany(targetEntity = Customer.class, cascade = CascadeType.ALL)
//	@JoinColumn(name = "customer_id", referencedColumnName = "customer_id", nullable = true)
//	private List<Car> cars;
}
