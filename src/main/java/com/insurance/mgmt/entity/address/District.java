package com.insurance.mgmt.entity.address;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DİSTRİCT")
public class District {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "ilceadi")
	private String ilceadi;
	@Column(name = "sehirid")
	private String sehirid;
	
	public District() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public District(int id, String ilceadi, String sehirid) {
		super();
		this.id = id;
		this.ilceadi = ilceadi;
		this.sehirid = sehirid;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIlceadi() {
		return ilceadi;
	}
	public void setIlceadi(String ilceadi) {
		this.ilceadi = ilceadi;
	}
	public String getSehirid() {
		return sehirid;
	}
	public void setSehirid(String sehirid) {
		this.sehirid = sehirid;
	}
	
	
	
}
