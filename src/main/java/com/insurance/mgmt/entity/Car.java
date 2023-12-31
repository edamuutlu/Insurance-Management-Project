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
	@Column(name="car_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name ="customer_id") //insertable = false, nullable = true
	private int customerId;
	
	@Column(name ="plate1")
	@NotNull(message = "This field must be filled.")
	//@Digits(integer=2, fraction=0, message= "Only numeric input is allowed.")
	//@Size(min = 1, max = 2, message = "Input must be 1 or 2 digits.") 
	private Integer plate1;
	
	
	@Column(name ="plate2")
	@NotBlank(message = "This field must be filled.")
	@Pattern(regexp = "[a-zA-Z\\s]+", message = "Only alphabetic characters and spaces are allowed")
	@Length(min = 1, max=3, message = "This field must be between 1 and 3 characters") 
	private String plate2;
	
	@Column(name ="plate3")
	@NotNull(message = "This field must be filled.")
	//@Digits(integer=4, fraction=0, message= "Only numeric input is allowed.")
	//@Size(min = 1, max = 4, message = "Input must be 1 or 4 digits.")
	private Integer plate3;
	
	@Column(name ="type")
	@NotBlank(message = "This field must be filled.")
	private String type;
	
	@Column(name ="purpose")
	@NotBlank(message = "This field must be filled.")
	private String purpose;
	
	@Column(name ="brand")
	@NotBlank(message = "This field must be filled.")
	private String brand;
	
	@Column(name ="fuel_type")
	@NotBlank(message = "This field must be filled.")
	private String fuelType;
	
	@Column(name ="engine_size")
	@NotNull(message = "Enter your engine size.")
	@Digits(integer=4, fraction=0, message= "Only numeric input is allowed.") 
	private Integer engineSize;
	
	@Column(name ="seat_capacity")
	@NotNull(message = "Enter your seating capacity.")
	@Digits(integer=10, fraction=0, message= "Only numeric input is allowed.")
	//@Size(min = 1,  message = "Input must be at least 1 digits.")
	private Integer seatCapacity;
	
	@Column(name= "period")
	private int period;
	
	@Column(name ="status")
	private int status;

	
	public Car() {
		super();
		// TODO Auto-generated constructor stub
	}
		

	public Car(int id, int customerId, @NotNull(message = "This field must be filled.") Integer plate1,
			@NotBlank(message = "This field must be filled.") @Pattern(regexp = "[a-zA-Z\\s]+", message = "Only alphabetic characters and spaces are allowed") @Length(min = 1, max = 3, message = "This field must be between 1 and 3 characters") String plate2,
			@NotNull(message = "This field must be filled.") Integer plate3,
			@NotBlank(message = "This field must be filled.") String type,
			@NotBlank(message = "This field must be filled.") String purpose,
			@NotBlank(message = "This field must be filled.") String brand,
			@NotBlank(message = "This field must be filled.") String fuelType,
			@NotNull(message = "Enter your engine size.") @Digits(integer = 4, fraction = 0, message = "Only numeric input is allowed.") Integer engineSize,
			@NotNull(message = "Enter your seating capacity.") @Digits(integer = 10, fraction = 0, message = "Only numeric input is allowed.") Integer seatCapacity,
			int period, int status) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.plate1 = plate1;
		this.plate2 = plate2;
		this.plate3 = plate3;
		this.type = type;
		this.purpose = purpose;
		this.brand = brand;
		this.fuelType = fuelType;
		this.engineSize = engineSize;
		this.seatCapacity = seatCapacity;
		this.period = period;
		this.status = status;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getCustomerId() {
		return customerId;
	}


	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}


	public Integer getPlate1() {
		return plate1;
	}


	public void setPlate1(Integer plate1) {
		this.plate1 = plate1;
	}


	public String getPlate2() {
		return plate2;
	}


	public void setPlate2(String plate2) {
		this.plate2 = plate2;
	}


	public Integer getPlate3() {
		return plate3;
	}


	public void setPlate3(Integer plate3) {
		this.plate3 = plate3;
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


	public String getFuelType() {
		return fuelType;
	}


	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

 
	public Integer getEngineSize() {
		return engineSize;
	}


	public void setEngineSize(Integer engineSize) {
		this.engineSize = engineSize;
	}


	public Integer getSeatCapacity() {
		return seatCapacity;
	}


	public void setSeatCapacity(Integer seatCapacity) {
		this.seatCapacity = seatCapacity;
	}

	
	
	public int getPeriod() {
		return period;
	}


	public void setPeriod(int period) {
		this.period = period;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "Car [id=" + id + ", customerId=" + customerId + ", plate1=" + plate1 + ", plate2=" + plate2
				+ ", plate3=" + plate3 + ", type=" + type + ", purpose=" + purpose + ", brand=" + brand + ", fuelType="
				+ fuelType + ", engineSize=" + engineSize + ", seatCapacity=" + seatCapacity + ", period=" + period
				+ ", status=" + status + "]";
	}	
	
		
}
