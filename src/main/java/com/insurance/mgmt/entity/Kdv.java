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
	@Column(name = "product_type")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int productType;
	
	@Column(name = "kdv_rate")
	private int kdvRate;

	public Kdv() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Kdv(int productType, int kdvRate) {
		super();
		this.productType = productType;
		this.kdvRate = kdvRate;
	}

	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
	}

	public int getKdvRate() {
		return kdvRate;
	}

	public void setKdvRate(int kdvRate) {
		this.kdvRate = kdvRate;
	}

	@Override
	public String toString() {
		return "Kdv [productType=" + productType + ", kdvRate=" + kdvRate + "]";
	}

	
	
}
