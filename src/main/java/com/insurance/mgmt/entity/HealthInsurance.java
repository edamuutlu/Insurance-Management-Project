package com.insurance.mgmt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "health_insurance")
public class HealthInsurance {
	@Id
	@Column(name ="insurance_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int insuranceId;
	
	@Column(name ="insurance_type") 
	private String insuranceType;
	
	@Column(name ="customer_id") 
	private int customerId;
	
	@Column(name ="health_id") 
	private int healthId;
	
	@Column(name ="offer") 
	private double offer;
	
	@Column(name ="result") 
	private String result;
	
	@Column(name ="start_date") 
	private String startDate;
	
	@Column(name ="end_date") 
	private String endDate;
	
	@Column(name ="period") 
	private int period;
	
	@Column(name ="days_diff") 
	private int daysDiff;
	
	@Column(name ="refund") 
	private double refund;
	
	@Column(name ="status") 
	private int status;
	
	@Column(name= "kdv")
	private int kdv;
	
	@Column(name= "company_id")
	private int companyId;

	public HealthInsurance() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HealthInsurance(int insuranceId, String insuranceType, int customerId, int healthId, double offer,
			String result, String startDate, String endDate, int period, int daysDiff, double refund, int status,
			int kdv, int companyId) {
		super();
		this.insuranceId = insuranceId;
		this.insuranceType = insuranceType;
		this.customerId = customerId;
		this.healthId = healthId;
		this.offer = offer;
		this.result = result;
		this.startDate = startDate;
		this.endDate = endDate;
		this.period = period;
		this.daysDiff = daysDiff;
		this.refund = refund;
		this.status = status;
		this.kdv = kdv;
		this.companyId = companyId;
	}

	public int getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(int insuranceId) {
		this.insuranceId = insuranceId;
	}

	public String getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getHealthId() {
		return healthId;
	}

	public void setHealthId(int healthId) {
		this.healthId = healthId;
	}

	public double getOffer() {
		return offer;
	}

	public void setOffer(double offer) {
		this.offer = offer;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getDaysDiff() {
		return daysDiff;
	}

	public void setDaysDiff(int daysDiff) {
		this.daysDiff = daysDiff;
	}

	public double getRefund() {
		return refund;
	}

	public void setRefund(double refund) {
		this.refund = refund;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getKdv() {
		return kdv;
	}

	public void setKdv(int kdv) {
		this.kdv = kdv;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	@Override
	public String toString() {
		return "HealthInsurance [insuranceId=" + insuranceId + ", insuranceType=" + insuranceType + ", customerId="
				+ customerId + ", healthId=" + healthId + ", offer=" + offer + ", result=" + result + ", startDate="
				+ startDate + ", endDate=" + endDate + ", period=" + period + ", daysDiff=" + daysDiff + ", refund="
				+ refund + ", status=" + status + ", kdv=" + kdv + ", companyId=" + companyId + "]";
	}
	
}
