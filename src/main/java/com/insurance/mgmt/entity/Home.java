package com.insurance.mgmt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "HOME")
public class Home {
	@Id
	@Column(name ="home_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int homeId;
	
	@Column(name ="customer_id") 
	private int customerId;
	
	@Column(name ="type") 
	private String type;
	
	@Column(name ="usage_type") 
	private String typeOfUse;
	
	@Column(name ="province") 
	private String province;
	
	@Column(name ="district") 
	private String district;
	
	@Column(name ="neighbourhood") 
	private String neighbourhood;
	
	@Column(name ="building_number") 
	private int buildingNumber;
	
	@Column(name ="apartment") 
	private int apartment;
	
	@Column(name ="floor") 
	private int floor;
	
	@Column(name ="building_age") 
	private int buildingAge;
	
	@Column(name ="owner_title") 
	private String ownerTitle;
	
	@Column(name ="status") 
	private int status;
		
	public Home() {
		super();
		// TODO Auto-generated constructor stub
	}		

	public Home(int homeId, int customerId, String type, String typeOfUse, String province, String district, String neighbourhood,
			int buildingNumber, int apartment, int floor, int buildingAge, String ownerTitle, int status) {
		super();
		this.homeId = homeId;
		this.customerId = customerId;
		this.type = type;
		this.typeOfUse = typeOfUse;
		this.province = province;
		this.district = district;
		this.neighbourhood = neighbourhood;
		this.buildingNumber = buildingNumber;
		this.apartment = apartment;
		this.floor = floor;
		this.buildingAge = buildingAge;
		this.ownerTitle = ownerTitle;
		this.status = status;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getBuildingAge() {
		return buildingAge;
	}

	public void setBuildingAge(int buildingAge) {
		this.buildingAge = buildingAge;
	}

	public String getOwnerTitle() {
		return ownerTitle;
	}

	public void setOwnerTitle(String ownerTitle) {
		this.ownerTitle = ownerTitle;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Home [homeId=" + homeId + ", customerId=" + customerId + ", type=" + type + ", usage=" + typeOfUse
				+ ", province=" + province + ", district=" + district + ", neighbourhood=" + neighbourhood
				+ ", buildingNumber=" + buildingNumber + ", apartment=" + apartment + ", floor=" + floor
				+ ", buildingAge=" + buildingAge + ", ownerTitle=" + ownerTitle + ", status=" + status + "]";
	}
	
	
}
