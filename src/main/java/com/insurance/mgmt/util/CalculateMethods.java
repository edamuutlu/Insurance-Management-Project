package com.insurance.mgmt.util;

import com.insurance.mgmt.entity.Car;
import com.insurance.mgmt.entity.Home;

public class CalculateMethods {
	
	public int calculate(Car car, int age) {
		// Sigorta teklifi için hesaplamalar
				int offer=500; // Sabit bir başlangıç teklifi-primi								
		        
		        if(age>18 && age<=25) {
		        	offer+= age * 4;
		        }else if (age>25 && age<=60) {
		        	offer+= age * 3;
				}else {
					offer+= age * 5;
				}
				
				switch (car.getType()) {	// Araç tipi için ek prim
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
			
				switch (car.getPurpose()) {	// Kullanım amacı için ek prim
				    case "Private":
				        offer += 400;
				        break;
				    case "Commercial":
				        offer += 500;
				        break;
				    default:
						break;
				}
				
				switch (car.getBrand()) {	// Araç modeli için ek prim
					case "Audi":
					case "BMW":
					case "Lamborghini":
					case "Mercedes":
					case "Volvo":
						offer+=3000;
						break;
					case "Citroen":
					case "Dacia":
					case "Hyundai":
					case "Kia":
					case "Jeep":
						offer+=2000;
						break;
					case "Fiat":
					case "Nissan":
					case "Opel":
					case "Renault":
					case "Skoda":
						offer+=1000;
						break;
					default:
						break;
				}
				
				// Sigortanın kapsadığı süreye bağlı olarak ek prim
				offer += car.getPeriod() * 20; 
				
				switch (car.getFuelType()) {	// Araç yakıt tipi için ek prim
					case "Petrol":
						offer+=300;
						break;
					case "Diesel":
						offer+=200;
						break;
					case "LPG":
						offer+=200;
						break;
					case "Electric":
						offer+=200;
						break;
					default:
						break;
				}
				
				if(car.getEngineSize()<2) {	// Motor hacmine göre ek prim
					offer+= (car.getEngineSize()) * 100;
				}else if(car.getEngineSize()>=2) {
					offer+=(car.getEngineSize()) * 200;
				}
				
				switch (car.getPlate1()) {	// Aracın kayıtlı olduğu ilin trafik yoğunluğuna göre ek prim
				case 06:
					offer+=400;
					break;
				case 16:
					offer+=300;
					break;
				case 34:
					offer+=500;
					break;
			    default:
					System.out.println("Geçerli olmayan il");
					break;
				}
				
				if(car.getSeatCapacity()<=2) {	// Aracın koltuk kapasitesine göre ek prim
					offer+= (car.getSeatCapacity()) * 20;
				}else if(car.getSeatCapacity()>2 && car.getSeatCapacity()<=5) {
					offer+= (car.getSeatCapacity()) * 30;
				}else { 
					offer+= (car.getSeatCapacity()) * 40;
				}
				return offer;
	}
	
	public int calculateHomeInsurance(Home home) {
		int offer = 10;
		return offer;
	}
}
