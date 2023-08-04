package com.insurance.mgmt.InsuranceManagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.insurance.mgmt.entity.Car;
import com.insurance.mgmt.service.CarService;
import com.insurance.mgmt.util.CalculateMethods;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class CarServiceTest {
	
	@MockBean
	private CarService carService;
	
	private List<Car> dummyCars = Stream.of(
            new Car(34, "vs", 1234, "Car", "Private", "Audi", "Petrol", 2, 5, 1, 6000, "Accepted", 1, 30, "17/05/2023 13:27:29", 0, 0, "17/06/2023 13:27:30", 10),
            new Car(34, "vs", 5678, "Car", "Private", "Audi", "Petrol", 2, 5, 1, 8000, "Accepted", 1, 30, "17/05/2023 13:00:00", 0, 0, "17/06/2023 13:00:00", 10)
    ).collect(Collectors.toList());

	@Test
	public void calculateCarInsuranceTest() {

		Car car = new Car();
	    car.setCustomerId(12); // Set customer_id property
	    car.setType("Car");
	    car.setPurpose("Private");
	    car.setBrand("Audi");
	    car.setFuelType("Petrol");
	    car.setEngineSize(2);
	    car.setPeriod(30);
	    car.setPlate1(34);
	    car.setSeatCapacity(4);	  

	    CalculateMethods calculateMethods = new CalculateMethods();
	    int KdvRate = 10;
	    double offer = calculateMethods.calculateCarInsurance(car, 20, KdvRate);	
    
	    double kdv = (((500 + (20 * 4) + 400 + 400 + 3000 + 600 + 300 + (2 * 200) + 500 + (4 * 30)) * KdvRate) / 100);
	    double expectedOffer = 500 + (20 * 4) + 400 + 400 + 3000 + 600 + 300 + (2 * 200) + 500 + (4 * 30) + kdv;
	    assertEquals(expectedOffer, offer);		

	}
	
	// CarService Sınıfındaki Metotların Testleri
		@Test
		public void saveCarTest() {
			 Car car = new Car(34, "vs", 1234, "Car", "Private", "Audi", "Petrol", 2, 5, 1, 6000, "Accepted", 1, 30, "17/05/2023 13:27:29", 0, 0, "17/06/2023 13:27:30", 10);
			 carService.save(car);
			 verify(carService, times(1)).save(car);
		}
		
		// Yöntem 1
		@Test
		public void getAllCarsTest() {
			when(carService.getAllCars()).thenReturn(Stream
					.of(new Car(34, "vs", 1234, "Car", "Private", "Audi", "Petrol", 2, 5, 1, 6000, "Accepted", 1, 30, "17/05/2023 13:27:29", 0, 0, "17/06/2023 13:27:30", 10), 
							(new Car(34, "vs", 5678, "Car", "Private", "Audi", "Petrol", 2, 5, 1, 8000, "Accepted", 1, 30, "17/05/2023 13:00:00", 0, 0, "17/06/2023 13:00:00", 10))).collect(Collectors.toList()));
			assertEquals(2, carService.getAllCars().size());
		}
		// Yöntem 2
		@Test
	    public void testGetAllCars() {
	        when(carService.getAllCars()).thenReturn(dummyCars);

	        List<Car> result = carService.getAllCars();
	        
	        verify(carService, times(1)).getAllCars();
	        assertEquals(dummyCars, result);
	    }

	    @Test
	    public void testDeleteById() {
	        int carId = 1;
	        carService.deleteById(carId);
	        verify(carService, times(1)).deleteById(carId);
	    }

	    @Test
	    public void testGetCarId() {
	        int carId = 1;
	        Car dummyCar = new Car();

	        when(carService.getCarId(carId)).thenReturn(dummyCar);
	        Car result = carService.getCarId(carId);
	        verify(carService, times(1)).getCarId(carId);
	        assertEquals(dummyCar, result);
	    }
	    
	    @Test
	    public void findByStatusTest() {
	        int status = 1;
	        when(carService.findByStatus(status)).thenReturn(dummyCars);
	        List<Car> result = carService.findByStatus(status);
	        assertEquals(dummyCars, result);
	        verify(carService, times(1)).findByStatus(status);
	    }
	    
	    @Test
	    public void findByStatusAndCustomerId() {
	        int status = 1;
	        when(carService.findByStatusAndCustomerId(status, 1)).thenReturn(dummyCars);
	        List<Car> result = carService.findByStatusAndCustomerId(status, 1);
	        assertEquals(dummyCars, result);
	        verify(carService, times(1)).findByStatusAndCustomerId(status, 1);
	    }
	    
	    @Test
	    public void findByStatusAndPlate1AndPlate2AndPlate3AndResult() {
	        int status = 1;
	        when(carService.findByStatusAndPlate1AndPlate2AndPlate3AndResult(status, 34, "vs", 1234, "Accepted")).thenReturn(dummyCars);
	        List<Car> result = carService.findByStatusAndPlate1AndPlate2AndPlate3AndResult(status, 34, "vs", 1234, "Accepted");
	        assertEquals(dummyCars, result);
	        verify(carService, times(1)).findByStatusAndPlate1AndPlate2AndPlate3AndResult(status, 34, "vs", 1234, "Accepted");
	    }
	    
	    @Test
	    public void findByStatusAndCustomerIdAndResult() {
	        int status = 1;
	        when(carService.findByStatusAndCustomerIdAndResult(status, 1, "Accepted")).thenReturn(dummyCars);
	        List<Car> result = carService.findByStatusAndCustomerIdAndResult(status, 1, "Accepted");
	        assertEquals(dummyCars, result);
	        verify(carService, times(1)).findByStatusAndCustomerIdAndResult(status, 1, "Accepted");
	    }

}
