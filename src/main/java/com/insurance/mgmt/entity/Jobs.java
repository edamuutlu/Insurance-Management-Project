package com.insurance.mgmt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "JOBS")
public class Jobs {
	@Id
	@Column(name = "job")
    private String job;
	
	@Column(name = "risk_factor")
    private int riskFactor;
    
	public Jobs() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Jobs(String job, int riskFactor) {
		super();
		this.job = job;
		this.riskFactor = riskFactor;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public int getRiskFactor() {
		return riskFactor;
	}

	public void setRiskFactor(int riskFactor) {
		this.riskFactor = riskFactor;
	}

	@Override
	public String toString() {
		return "Jobs [job=" + job + ", riskFactor=" + riskFactor + "]";
	}
    
    
}
