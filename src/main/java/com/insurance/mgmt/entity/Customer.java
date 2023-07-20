package com.insurance.mgmt.entity;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
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
	private int customerId;

	@Column(name="tc")
	@NotEmpty(message = "Enter your tc number.")
	@Digits(integer=11, fraction=0, message= "Only numeric input is allowed.")
	@Size(min = 11, max = 11, message = "TC number must be 11 digits.")
	private String tc;

	@Column(name="birth")
	@NotBlank(message = "Enter your birth.")
	//@Past(message = "Enter a past date")
	@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Enter a valid date in the format yyyy-mm-dd")
	private String birth;

	@Column(name="email")
	@NotBlank(message = "Enter your email.")
	@Email(message = "Enter a valid email address.")
	private String email;

	@Column(name="firstname")
	@NotBlank(message = "Enter your first name.")
	@Pattern(regexp = "[a-zA-Z\\s]+", message = "Only alphabetic characters and spaces are allowed")
	@Length(min = 2, message = "Name must be at least 2 characters")
	private String firstname;
	
	@Column(name="lastname")
	@NotBlank(message = "Enter your last name")
	@Pattern(regexp = "[a-zA-Z\\s]+", message = "Only alphabetic characters and spaces are allowed")
	@Length(min = 2, message = "Name must be at least 2 characters")
	private String lastname;
	
	@Column(name="province")
	@NotBlank(message = "Enter your province.")
	private String province;
	
	@Column(name="district")
	@NotBlank(message = "Enter your district.")
	private String district;
	
	@Column(name="status")
	private int status;

	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Customer(int customerId, String tc, String birth, String email, String firstname, String lastname,
			String province, String district, int status) {
		super();
		this.customerId = customerId;
		this.tc = tc;
		this.birth = birth;
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.province = province;
		this.district = district;
		this.status = status;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", tc=" + tc + ", birth=" + birth + ", email=" + email
				+ ", firstname=" + firstname + ", lastname=" + lastname + ", province=" + province + ", district="
				+ district + ", status=" + status + "]";
	}

	

//	@OneToMany(targetEntity = Customer.class, cascade = CascadeType.ALL)
//	@JoinColumn(name = "customer_id", referencedColumnName = "customer_id", nullable = true)
//	private List<Car> cars;
}
