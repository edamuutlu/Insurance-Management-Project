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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int home_id;
	
	@Column(name ="customer_id") 
	private int customerId;
	
	@Column(name ="type") 
	private int type;
	
	@Column(name ="usage") 
	private int usage;
	
	@Column(name ="province") 
	private int province;
	
	@Column(name ="district") 
	private int district;
	
	@Column(name ="neighbourhood") 
	private int neighbourhood;
	
	@Column(name ="building_number") 
	private int buildingNumber;
	
	@Column(name ="apartment") 
	private int apartment;
	
	@Column(name ="floor") 
	private int floor;
	
	@Column(name ="building_age") 
	private int buildingAge;
	
	@Column(name ="owner_title") 
	private int ownerTitle;
	
	@Column(name ="status") 
	private int status;
		
	public Home() {
		super();
		// TODO Auto-generated constructor stub
	}		

	public Home(int home_id, int customerId, int type, int usage, int province, int district, int neighbourhood,
			int buildingNumber, int apartment, int floor, int buildingAge, int ownerTitle, int status) {
		super();
		this.home_id = home_id;
		this.customerId = customerId;
		this.type = type;
		this.usage = usage;
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


	public int getHome_id() {
		return home_id;
	}

	public void setHome_id(int home_id) {
		this.home_id = home_id;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getUsage() {
		return usage;
	}

	public void setUsage(int usage) {
		this.usage = usage;
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getDistrict() {
		return district;
	}

	public void setDistrict(int district) {
		this.district = district;
	}

	public int getNeighbourhood() {
		return neighbourhood;
	}

	public void setNeighbourhood(int neighbourhood) {
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

	public int getOwnerTitle() {
		return ownerTitle;
	}

	public void setOwnerTitle(int ownerTitle) {
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
		return "Home [home_id=" + home_id + ", customerId=" + customerId + ", type=" + type + ", usage=" + usage
				+ ", province=" + province + ", district=" + district + ", neighbourhood=" + neighbourhood
				+ ", buildingNumber=" + buildingNumber + ", apartment=" + apartment + ", floor=" + floor
				+ ", buildingAge=" + buildingAge + ", ownerTitle=" + ownerTitle + ", status=" + status + "]";
	}
	
	
}
