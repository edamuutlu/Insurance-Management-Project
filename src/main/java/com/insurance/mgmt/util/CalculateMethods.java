package com.insurance.mgmt.util;

import com.insurance.mgmt.entity.Car;
import com.insurance.mgmt.entity.Health;
import com.insurance.mgmt.entity.Home;

public class CalculateMethods {

	public static double calculateCarInsurance(Car car, int age, int kdvRate) {
		// Sigorta teklifi için hesaplamalar
		double offer = 500; // Sabit bir başlangıç teklifi-primi

		if (age > 18 && age <= 25) {
			offer += age * 4;
		} else if (age > 25 && age <= 60) {
			offer += age * 3;
		} else {
			offer += age * 5;
		}

		switch (car.getType()) { // Araç tipi için ek prim
		case "Car":
			offer += 400;
			break;
		case "Truck":
			offer += 500;
			break;
		case "Van":
			offer += 300;
			break;
		default:
			break;
		}

		switch (car.getPurpose()) { // Kullanım amacı için ek prim
		case "Private":
			offer += 400;
			break;
		case "Commercial":
			offer += 500;
			break;
		default:
			break;
		}

		switch (car.getBrand()) { // Araç modeli için ek prim
		case "Audi":
		case "BMW":
		case "Lamborghini":
		case "Mercedes":
		case "Volvo":
			offer += 3000;
			break;
		case "Citroen":
		case "Dacia":
		case "Hyundai":
		case "Kia":
		case "Jeep":
			offer += 2000;
			break;
		case "Fiat":
		case "Nissan":
		case "Opel":
		case "Renault":
		case "Skoda":
			offer += 1000;
			break;
		default:
			break;
		}

		// Sigortanın kapsadığı süreye bağlı olarak ek prim
		offer += car.getPeriod() * 20;

		switch (car.getFuelType()) { // Araç yakıt tipi için ek prim
		case "Petrol":
			offer += 300;
			break;
		case "Diesel":
			offer += 200;
			break;
		case "LPG":
			offer += 200;
			break;
		case "Electric":
			offer += 200;
			break;
		default:
			break;
		}

		if (car.getEngineSize() < 2) { // Motor hacmine göre ek prim
			offer += (car.getEngineSize()) * 100;
		} else if (car.getEngineSize() >= 2) {
			offer += (car.getEngineSize()) * 200;
		}

		switch (car.getPlate1()) { // Aracın kayıtlı olduğu ilin trafik yoğunluğuna göre ek prim
		case 06:
			offer += 400;
			break;
		case 35:
			offer += 300;
			break;
		case 34:
			offer += 500;
			break;
		default:
			offer += 200;
			break;
		}

		if (car.getSeatCapacity() <= 2) { // Aracın koltuk kapasitesine göre ek prim
			offer += (car.getSeatCapacity()) * 20;
		} else if (car.getSeatCapacity() > 2 && car.getSeatCapacity() <= 5) {
			offer += (car.getSeatCapacity()) * 30;
		} else {
			offer += (car.getSeatCapacity()) * 40;
		}

		// KDV oranı teklife eklenmektedir
		offer += (offer * kdvRate) / 100;

		return offer;
	}

	public static double calculateHomeInsurance(Home home, int kdvRate) {
		double offer = 500;

		if (home.getFlatArea() <= 50) { // Konutun metrekaresine göre ek prim
			offer += home.getFlatArea() * 3;
		} else if (home.getFlatArea() > 50 && home.getFlatArea() <= 100) {
			offer += home.getFlatArea() * 4;
		} else {
			offer += home.getFlatArea() * 5;
		}

		switch (home.getBuildingType()) { // Konutun yapı tarzına göre ek prim
		case "Reinforced Concrete":
			offer += 500;
			break;
		case "Other Structures":
			offer += 1000;
			break;
		default:
			break;
		}

		switch (home.getTypeOfUse()) { // Konutun yapı tarzına göre ek prim
		case "Summer House":
			offer += 500;
			break;
		case "Permanent Residence":
			offer += 1000;
			break;
		default:
			break;
		}

		switch (home.getProvince()) { // Konutun bulunduğu ile göre ek prim
		case "6":
			offer += 800;
			break;
		case "35":
			offer += 700;
			break;
		case "34":
			offer += 1000;
			break;
		default:
			offer += 500;
			break;
		}

		if (home.getBuildingAge() <= 5) { // Bina yaşına göre ek prim
			offer += home.getBuildingAge() * 10;
		} else if (home.getBuildingAge() > 5 && home.getBuildingAge() <= 10) {
			offer += home.getBuildingAge() * 20;
		} else {
			offer += home.getBuildingAge() * 30;
		}

		switch (home.getFloor()) { // Binanın kat sayısına göre ek prim
		case "Basement":
			offer += 500;
			break;
		case "Ground Floor":
			offer += 600;
			break;
		case "1-10 Floor":
			offer += 700;
			break;
		case "11-20 Floor":
			offer += 800;
			break;
		case "20 or More Floors":
			offer += 900;
			break;
		default:
			break;
		}

		switch (home.getInsurerTitle()) {
		case "Owner":
			offer += 400;
			break;
		case "Tenant":
			offer += 200;
			break;
		default:
			break;
		}

		// Sigortanın kapsadığı süreye bağlı olarak ek prim
		offer += home.getPeriod() * 20;

		// KDV oranı teklife eklenmektedir
		offer += (offer * kdvRate) / 100;

		return offer;
	}
	

	public static double calculateHealthInsurance(Health health, int kdvRate, int riskFactor) {
		double offer = 500;
		
		switch (riskFactor) {
		case 3:
			offer += 400;
			break;
		case 2:
			offer += 200;
			break;
		case 1:
			offer += 100;
			break;
		default:
			break;
		}

		switch (health.getForWho()) {
		case "Me":
			offer += 200;
			break;
		case "My Family":
			offer += 400;
			break;
		case "My Children":
			offer += 400;
			break;
		default:
			break;
		}
		
		if(health.getSgk() == 1) {
			offer += 200;
		}else {
			offer += 400;
		}
		
		if(health.getPastOperation() == 1) {
			offer += 200;
		}else {
			offer += 400;
		}
		
		// Beden Kütle İndeksine göre ek prim
		double bki = (Integer.parseInt(health.getWeight()) / Math.pow(Integer.parseInt(health.getHeight()),2));
		
		if(bki < 18.5 || bki > 25) {
			offer += 400;
		}else {
			offer += 200;
		}
		
		if(health.getSmokingOrAlcohol() == 1) {
			offer += 400;
		}else {
			offer += 200;
		}

		// Sigortanın kapsadığı süreye bağlı olarak ek prim
		offer += health.getPeriod() * 20;

		// KDV oranı teklife eklenmektedir
		offer += (offer * kdvRate) / 100;

		return offer;
	}
}
