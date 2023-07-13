package com.insurance.mgmt.entity;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "CAR")
public class Car {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name ="customer_id") //insertable = false, nullable = true
	private int customer_id;
	
	@Column(name ="license_plate1")
	@NotNull(message = "This field must be filled.")
	//@Digits(integer=2, fraction=0, message= "Only numeric input is allowed.")
	//@Size(min = 1, max = 2, message = "Input must be 1 or 2 digits.") 
	private int license_plate1;
	
	
	@Column(name ="license_plate2")
	@NotBlank(message = "This field must be filled.")
	@Pattern(regexp = "[a-zA-Z\\s]+", message = "Only alphabetic characters and spaces are allowed")
	@Length(min = 1, max=3, message = "This field must be between 1 and 3 characters") 
	private String license_plate2;
	
	@Column(name ="license_plate3")
	@NotNull(message = "This field must be filled.")
	@Digits(integer=4, fraction=0, message= "Only numeric input is allowed.")
	//@Size(min = 1, max = 4, message = "Input must be 1 or 4 digits.") 
	private int license_plate3;
	
	@NotBlank(message = "This field must be filled.")
	private String type;
	
	@NotBlank(message = "This field must be filled.")
	private String purpose;
	
	@NotBlank(message = "This field must be filled.")
	private String brand;
	
	@Column(name ="fuel_type")
	@NotBlank(message = "This field must be filled.")
	private String fuel_type;
	
	@Column(name ="engine_size")
	@NotNull(message = "Enter your engine size.")
	@Digits(integer=4, fraction=0, message= "Only numeric input is allowed.") 
	private int engine_size;
	
	@Column(name ="seat_capacity")
	@NotNull(message = "Enter your seating capacity.")
	@Digits(integer=10, fraction=0, message= "Only numeric input is allowed.")
	//@Size(min = 1,  message = "Input must be at least 1 digits.")
	private int seat_capacity;
	
	@Column(name ="offer") //insertable = false, nullable = true
	private int offer;
	
	private String result;
	
	private int status;
	
	private int period;
	
	private String starting_date;
	
	public Car() {
		super();
		// TODO Auto-generated constructor stub
	}
		

	public Car(int license_plate1, String license_plate2, int license_plate3, String type,
			String purpose, String brand, String fuel_type, int engine_size, int seat_capacity, int customer_id, int offer, String result, int status, int period, String starting_date) {
		super();
		this.customer_id = customer_id;
		this.license_plate1 = license_plate1;
		this.license_plate2 = license_plate2;
		this.license_plate3 = license_plate3;
		this.type = type;
		this.purpose = purpose;
		this.brand = brand;
		this.fuel_type = fuel_type;
		this.engine_size = engine_size;
		this.seat_capacity = seat_capacity;
		this.offer=offer;
		this.result=result;
		this.status = status;
		this.period=period;
		this.starting_date = starting_date;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getCustomer_id() {
		return customer_id;
	}


	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}


	public int getLicense_plate1() {
		return license_plate1;
	}


	public void setLicense_plate1(int license_plate1) {
		this.license_plate1 = license_plate1;
	}


	public String getLicense_plate2() {
		return license_plate2;
	}


	public void setLicense_plate2(String license_plate2) {
		this.license_plate2 = license_plate2;
	}


	public int getLicense_plate3() {
		return license_plate3;
	}


	public void setLicense_plate3(int license_plate3) {
		this.license_plate3 = license_plate3;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getPurpose() {
		return purpose;
	}


	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}


	public String getBrand() {
		return brand;
	}


	public void setBrand(String brand) {
		this.brand = brand;
	}


	public String getFuel_type() {
		return fuel_type;
	}


	public void setFuel_type(String fuel_type) {
		this.fuel_type = fuel_type;
	}

 
	public int getEngine_size() {
		return engine_size;
	}


	public void setEngine_size(int engine_size) {
		this.engine_size = engine_size;
	}


	public int getSeat_capacity() {
		return seat_capacity;
	}


	public void setSeat_capacity(int seat_capacity) {
		this.seat_capacity = seat_capacity;
	}


	public int getOffer() {
		return offer;
	}


	public void setOffer(int offer) {
		this.offer = offer;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}
	
	

	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}
		

	public int getPeriod() {
		return period;
	}


	public void setPeriod(int period) {
		this.period = period;
	}
		

	public String getStarting_date() {
		return starting_date;
	}


	public void setStarting_date(String starting_date) {
		this.starting_date = starting_date;
	}


	@Override
	public String toString() {
		return "Car [id=" + id + ", customer_id=" + customer_id + ", license_plate1=" + license_plate1
				+ ", license_plate2=" + license_plate2 + ", license_plate3=" + license_plate3 + ", type=" + type
				+ ", purpose=" + purpose + ", brand=" + brand + ", fuel_type=" + fuel_type + ", engine_size="
				+ engine_size + ", seat_capacity=" + seat_capacity + ", offer=" + offer + ", result=" + result
				+ ", status=" + status + ", period=" + period + ", starting_date=" + starting_date + "]";
	}
	
		
}
