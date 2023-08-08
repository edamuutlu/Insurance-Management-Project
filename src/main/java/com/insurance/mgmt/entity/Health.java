package com.insurance.mgmt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "HEALTH")
public class Health {
	@Id
	@Column(name ="health_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int healthId;
	
	@Column(name ="customer_id") 
	private int customerId;
	
	@Column(name ="job") 
	private String job;
	
	@Column(name ="for_who") 
	private String forWho;
	
	@Column(name ="sgk") 
	private Byte sgk;	// tinyint alanı için Byte veri tipi kullanılmaktadır
	
	@Column(name ="height") 
	private String height;
	
	@Column(name ="weight") 
	private String weight;
	
	@Column(name ="smoking_alcohol") 
	private Byte smokingOrAlcohol;
	
	@Column(name ="past_operation") 
	private Byte pastOperation;
	
	@Column(name ="status") 
	private int status;
	
	@Column(name= "period")
	private int period;

	public Health() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Health(int healthId, int customerId, String job, String forWho, Byte sgk, String height, String weight,
			Byte smokingOrAlcohol, Byte pastOperation, int status, int period) {
		super();
		this.healthId = healthId;
		this.customerId = customerId;
		this.job = job;
		this.forWho = forWho;
		this.sgk = sgk;
		this.height = height;
		this.weight = weight;
		this.smokingOrAlcohol = smokingOrAlcohol;
		this.pastOperation = pastOperation;
		this.status = status;
		this.period = period;
	}

	public int getHealthId() {
		return healthId;
	}

	public void setHealthId(int healthId) {
		this.healthId = healthId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getForWho() {
		return forWho;
	}

	public void setForWho(String forWho) {
		this.forWho = forWho;
	}

	public Byte getSgk() {
		return sgk;
	}

	public void setSgk(Byte sgk) {
		this.sgk = sgk;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Byte getSmokingOrAlcohol() {
		return smokingOrAlcohol;
	}

	public void setSmokingOrAlcohol(Byte smokingOrAlcohol) {
		this.smokingOrAlcohol = smokingOrAlcohol;
	}

	public Byte getPastOperation() {
		return pastOperation;
	}

	public void setPastOperation(Byte pastOperation) {
		this.pastOperation = pastOperation;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	@Override
	public String toString() {
		return "Health [healthId=" + healthId + ", customerId=" + customerId + ", job=" + job + ", forWho=" + forWho
				+ ", sgk=" + sgk + ", height=" + height + ", weight=" + weight + ", smokingOrAlcohol="
				+ smokingOrAlcohol + ", pastOperation=" + pastOperation + ", status=" + status + ", period=" + period
				+ "]";
	}
	
	
}
