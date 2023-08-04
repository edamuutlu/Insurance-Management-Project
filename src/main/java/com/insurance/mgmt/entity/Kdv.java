package com.insurance.mgmt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "KDV")
public class Kdv {
	@Id
	@Column(name = "product_type")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int productType;
	
	@Column(name = "kdv_rate")
	@NotNull(message = "Enter your kdv rate.")
	@Min(value = 0, message = "Kdv rate should not be less than 0.")
	@Max(value = 100, message = "Kdv rate should not be greater than 100.")
	private Integer kdvRate;


	public Kdv() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Kdv(int productType, Integer kdvRate) {
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

	public Integer getKdvRate() {
		return kdvRate;
	}

	public void setKdvRate(Integer kdvRate) {
		this.kdvRate = kdvRate;
	}

	@Override
	public String toString() {
		return "Kdv [productType=" + productType + ", kdvRate=" + kdvRate + "]";
	}

	
	
}
