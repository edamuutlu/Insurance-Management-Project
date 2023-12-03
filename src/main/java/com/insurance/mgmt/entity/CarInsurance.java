package com.insurance.mgmt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "car_insurance")
public class CarInsurance {
	@Id
	@Column(name ="insurance_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int insuranceId;
	
	@Column(name ="insurance_type") 
	private String insuranceType;
	
	@Column(name ="customer_id") 
	private int customerId;
	
	@Column(name ="car_id") 
	private int carId;
	
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

	public CarInsurance() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CarInsurance(int insuranceId, String insuranceType, int customerId, int carId, double offer, String result,
			String startDate, String endDate, int period, int daysDiff, double refund, int status, int kdv) {
		super();
		this.insuranceId = insuranceId;
		this.insuranceType = insuranceType;
		this.customerId = customerId;
		this.carId = carId;
		this.offer = offer;
		this.result = result;
		this.startDate = startDate;
		this.endDate = endDate;
		this.period = period;
		this.daysDiff = daysDiff;
		this.refund = refund;
		this.status = status;
		this.kdv = kdv;
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

	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
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

	@Override
	public String toString() {
		return "CarInsurance [insuranceId=" + insuranceId + ", insuranceType=" + insuranceType + ", customerId="
				+ customerId + ", carId=" + carId + ", offer=" + offer + ", result=" + result + ", startDate="
				+ startDate + ", endDate=" + endDate + ", period=" + period + ", daysDiff=" + daysDiff + ", refund="
				+ refund + ", status=" + status + ", kdv=" + kdv + "]";
	}
	
}
