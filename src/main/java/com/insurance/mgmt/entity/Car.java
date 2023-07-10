package com.insurance.mgmt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
	private int license_plate1;
	
	@Column(name ="license_plate2")
	private String license_plate2;
	
	@Column(name ="license_plate3")
	private int license_plate3;
	
	private String type;
	
	private String purpose;
	
	private String brand;
	
	@Column(name ="fuel_type")
	private String fuel_type;
	
	@Column(name ="engine_size")
	private int engine_size;
	
	@Column(name ="seat_capacity")
	private int seat_capacity;
	
	@Column(name ="offer") //insertable = false, nullable = true
	private int offer;
	
	public Car() {
		super();
		// TODO Auto-generated constructor stub
	}
		

	public Car(int license_plate1, String license_plate2, int license_plate3, String type,
			String purpose, String brand, String fuel_type, int engine_size, int seat_capacity, int customer_id, int offer) {
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
	
	
	
	
}
