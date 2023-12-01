package com.insurance.mgmt.entity.address;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mahalleler")
public class Neighbourhood {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mahalle_id")	
	private int mahalleId;
	@Column(name = "mahalle_adi")
	private String mahalleAdi;
	@Column(name = "ilce_id")	
	private int ilceId;
	@Column(name = "ilce_adi")
	private String ilceAdi;
	@Column(name = "il_id")
	private int sehirId;
	@Column(name = "il_adi")
	private String sehirAdi;
	
	public Neighbourhood() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Neighbourhood(int mahalleId, String mahalleAdi, int ilceId, String ilceAdi, int sehirId,
			String sehirAdi) {
		super();
		this.mahalleId = mahalleId;
		this.mahalleAdi = mahalleAdi;
		this.ilceId = ilceId;
		this.ilceAdi = ilceAdi;
		this.sehirId = sehirId;
		this.sehirAdi = sehirAdi;
	}

	public int getMahalleId() {
		return mahalleId;
	}

	public void setMahalleId(int mahalleId) {
		this.mahalleId = mahalleId;
	}

	public String getMahalleAdi() {
		return mahalleAdi;
	}

	public void setMahalleAdi(String mahalleAdi) {
		this.mahalleAdi = mahalleAdi;
	}

	public int getIlceId() {
		return ilceId;
	}

	public void setIlceId(int ilceId) {
		this.ilceId = ilceId;
	}

	public String getIlceAdi() {
		return ilceAdi;
	}

	public void setIlceAdi(String ilceAdi) {
		this.ilceAdi = ilceAdi;
	}

	public int getSehirId() {
		return sehirId;
	}

	public void setSehirId(int sehirId) {
		this.sehirId = sehirId;
	}

	public String getSehirAdi() {
		return sehirAdi;
	}

	public void setSehirAdi(String sehirAdi) {
		this.sehirAdi = sehirAdi;
	}

	@Override
	public String toString() {
		return "Neighbourhood [mahalleId=" + mahalleId + ", mahalleAdi=" + mahalleAdi + ", ilceId=" + ilceId
				+ ", ilceAdi=" + ilceAdi + ", sehirId=" + sehirId + ", sehirAdi=" + sehirAdi + "]";
	}		
	
}
