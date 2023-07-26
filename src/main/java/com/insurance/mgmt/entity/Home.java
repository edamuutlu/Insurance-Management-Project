package com.insurance.mgmt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "HOME")
public class Home {
	@Id
	@Column(name ="home_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int homeId;
	
	@Column(name ="customer_id") 
	private int customerId;
	
	@Column(name ="building_type")
	@NotBlank(message = "Enter your building type.")
	private String buildingType;
	
	@Column(name ="usage_type") 
	@NotBlank(message = "Enter your type of use.")
	private String typeOfUse;
	
	@Column(name ="province")
	@NotBlank(message = "Enter your province.")
	private String province;
	
	@Column(name ="district")
	@NotBlank(message = "Enter your district.")
	private String district;
	
	@Column(name ="neighbourhood") 
	@NotBlank(message = "Enter your neighbourhood.")
	@Pattern(regexp = "[a-zA-Z\\s]+", message = "Only alphabetic characters and spaces are allowed")
	private String neighbourhood;
	
	@Column(name ="building_number") 
	private int buildingNumber;
	
	@Column(name ="apartment") 
	private int apartment;
	
	@Column(name ="floor") 
	@NotBlank(message = "Enter your building floors.")
	private String floor;
	
	@Column(name ="building_age") 
	private int buildingAge;
	
	@Column(name ="insurer_title") 
	@NotBlank(message = "Enter your insurer title.")
	private String insurerTitle;
	
	@Column(name ="status") 
	private int status;
	
	@Column(name="flat_area")
	private int flatArea;
	
	@Column(name= "period")
	private int period;
	
	@Column(name= "year")
	private int year;
		
	public Home() {
		super();
		// TODO Auto-generated constructor stub
	}		

	public Home(int homeId, int customerId, String buildingType, String typeOfUse, String province, String district, String neighbourhood,
			int buildingNumber, int apartment, String floor, int buildingAge, String insurerTitle, int status, int period, int flatArea, int year) {
		super();
		this.homeId = homeId;
		this.customerId = customerId;
		this.buildingType = buildingType;
		this.typeOfUse = typeOfUse;
		this.province = province;
		this.district = district;
		this.neighbourhood = neighbourhood;
		this.buildingNumber = buildingNumber;
		this.apartment = apartment;
		this.floor = floor;
		this.buildingAge = buildingAge;
		this.insurerTitle = insurerTitle;
		this.status = status;
		this.flatArea = flatArea;
		this.period = period;
		this.year = year;
	}

	public int getHomeId() {
		return homeId;
	}

	public void setHomeId(int homeId) {
		this.homeId = homeId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}

	public String getTypeOfUse() {
		return typeOfUse;
	}

	public void setTypeOfUse(String typeOfUse) {
		this.typeOfUse = typeOfUse;
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

	public String getNeighbourhood() {
		return neighbourhood;
	}

	public void setNeighbourhood(String neighbourhood) {
		this.neighbourhood = neighbourhood;
	}

	public int getBuildingNumber() {
		return buildingNumber;
	}

	public void setBuildingNumber(int buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	public int getApartment() {
		return apartment;
	}

	public void setApartment(int apartment) {
		this.apartment = apartment;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public int getBuildingAge() {
		return buildingAge;
	}

	public void setBuildingAge(int buildingAge) {
		this.buildingAge = buildingAge;
	}

	public String getInsurerTitle() {
		return insurerTitle;
	}

	public void setInsurerTitle(String insurerTitle) {
		this.insurerTitle = insurerTitle;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getFlatArea() {
		return flatArea;
	}

	public void setFlatArea(int flatArea) {
		this.flatArea = flatArea;
	}		

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}	

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "Home [homeId=" + homeId + ", customerId=" + customerId + ", buildingType=" + buildingType
				+ ", typeOfUse=" + typeOfUse + ", province=" + province + ", district=" + district + ", neighbourhood="
				+ neighbourhood + ", buildingNumber=" + buildingNumber + ", apartment=" + apartment + ", floor=" + floor
				+ ", buildingAge=" + buildingAge + ", insurerTitle=" + insurerTitle + ", status=" + status
				+ ", flatArea=" + flatArea + ", period=" + period + ", year=" + year + "]";
	}	
	
}
