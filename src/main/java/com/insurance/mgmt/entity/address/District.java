package com.insurance.mgmt.entity.address;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ilceler")
public class District {
	@Id
	@Column(name = "ilce_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "ilce_adi")
	private String ilceadi;
	@Column(name = "il_id")
	private int sehirid;
	@Column(name = "il_adi")
	private String sehiradi;
	
	public District() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public District(int id, String ilceadi, int sehirid, String sehiradi) {
		super();
		this.id = id;
		this.ilceadi = ilceadi;
		this.sehirid = sehirid;
		this.sehiradi = sehiradi;
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
	public int getSehirid() {
		return sehirid;
	}
	public void setSehirid(int sehirid) {
		this.sehirid = sehirid;
	}

	public String getSehiradi() {
		return sehiradi;
	}

	public void setSehiradi(String sehiradi) {
		this.sehiradi = sehiradi;
	}

	@Override
	public String toString() {
		return "District [id=" + id + ", ilceadi=" + ilceadi + ", sehirid=" + sehirid + ", sehiradi=" + sehiradi + "]";
	}
	
	
	
}
