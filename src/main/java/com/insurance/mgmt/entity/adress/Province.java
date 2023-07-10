package com.insurance.mgmt.entity.adress;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Province {
	@Id
	@Column(name = "id")
	private int id;
	@Column(name = "sehiradi")
	private String sehiradi;
			
	public Province() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Province(int id, String sehiradi) {
		super();
		this.id = id;
		this.sehiradi = sehiradi;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSehiradi() {
		return sehiradi;
	}

	public void setSehiradi(String sehiradi) {
		this.sehiradi = sehiradi;
	}
	
	
}
