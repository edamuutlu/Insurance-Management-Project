package com.insurance.mgmt.entity.address;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sokaklar")
public class Street {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sokak_id")	
	private int sokakId;
	@Column(name = "mahalle_id")	
	private int mahalleId;
	@Column(name = "mahalle_adi")
	private String mahalleAdi;
	@Column(name = "ilce_id")	
	private int ilceId;
	@Column(name = "ilce_adi")
	private String ilceAdi;
	@Column(name = "il_id")
	private String sehirId;
	@Column(name = "il_adi")
	private String sehirAdi;
	
	public Street() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Street(int sokakId, int mahalleId, String mahalleAdi, int ilceId, String ilceAdi, String sehirId,
			String sehirAdi) {
		super();
		this.sokakId = sokakId;
		this.mahalleId = mahalleId;
		this.mahalleAdi = mahalleAdi;
		this.ilceId = ilceId;
		this.ilceAdi = ilceAdi;
		this.sehirId = sehirId;
		this.sehirAdi = sehirAdi;
	}

	public int getSokakId() {
		return sokakId;
	}

	public void setSokakId(int sokakId) {
		this.sokakId = sokakId;
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

	public String getSehirId() {
		return sehirId;
	}

	public void setSehirId(String sehirId) {
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
		return "Street [sokakId=" + sokakId + ", mahalleId=" + mahalleId + ", mahalleAdi=" + mahalleAdi + ", ilceId="
				+ ilceId + ", ilceAdi=" + ilceAdi + ", sehirId=" + sehirId + ", sehirAdi=" + sehirAdi + "]";
	}
	
	
}
