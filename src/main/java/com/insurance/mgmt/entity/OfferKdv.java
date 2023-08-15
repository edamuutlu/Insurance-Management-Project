package com.insurance.mgmt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "OFFER_KDV")
public class OfferKdv {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "product_type")
	private int productType;
	
	@Column(name = "kdv_rate")
	private Integer kdvRate;
	
	@Column(name = "last_customer_id")
	private int lastCustomerId;

	public OfferKdv() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OfferKdv(int id, int productType, Integer kdvRate, int lastCustomerId) {
		super();
		this.id = id;
		this.productType = productType;
		this.kdvRate = kdvRate;
		this.lastCustomerId = lastCustomerId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
	}

	public Integer getKdvRate() {
		return kdvRate;
	}

	public void setKdvRate(Integer kdvRate) {
		this.kdvRate = kdvRate;
	}

	public int getLastCustomerId() {
		return lastCustomerId;
	}

	public void setLastCustomerId(int lastCustomerId) {
		this.lastCustomerId = lastCustomerId;
	}

	@Override
	public String toString() {
		return "OldKdv [id=" + id + ", productType=" + productType + ", kdvRate=" + kdvRate + ", lastCustomerId="
				+ lastCustomerId + "]";
	}
	
	
}
