package com.insurance.mgmt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "KDV")
public class Kdv {
	@Id
	@Column(name = "kdv_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int kdvId;
	
	@Column(name = "car")
	private int carKdv;
	
	@Column(name = "home")
	private int homeKdv;

	public Kdv() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Kdv(int kdvId, int carKdv, int homeKdv) {
		super();
		this.kdvId = kdvId;
		this.carKdv = carKdv;
		this.homeKdv = homeKdv;
	}

	public int getKdvId() {
		return kdvId;
	}

	public void setKdvId(int kdvId) {
		this.kdvId = kdvId;
	}

	public int getCarKdv() {
		return carKdv;
	}

	public void setCarKdv(int carKdv) {
		this.carKdv = carKdv;
	}

	public int getHomeKdv() {
		return homeKdv;
	}

	public void setHomeKdv(int homeKdv) {
		this.homeKdv = homeKdv;
	}

	@Override
	public String toString() {
		return "Kdv [kdvId=" + kdvId + ", carKdv=" + carKdv + ", homeKdv=" + homeKdv + "]";
	}
	
}
