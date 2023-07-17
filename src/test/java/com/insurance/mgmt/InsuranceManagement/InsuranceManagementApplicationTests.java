package com.insurance.mgmt.InsuranceManagement;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.insurance.mgmt.controller.CarController;
import com.insurance.mgmt.entity.Car;

@SpringBootTest
class InsuranceManagementApplicationTests {

	@Test
	void contextLoads() {
		Car car = new Car();
        car.setPlate1(34);
        car.setType("Car");
        car.setPurpose("Private");
        car.setBrand("Kia");
        car.setFuel_type("Petrol");
        car.setEngine_size(3);
        car.setSeat_capacity(5);
        car.setPeriod(60);

        CarController carController = new CarController();

        int result1 = carController.calculate(car);
        System.out.println(result1);
        assertEquals(20450, result1);	
	}

}
