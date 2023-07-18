package com.insurance.mgmt.InsuranceManagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.insurance.mgmt.controller.CarController;
import com.insurance.mgmt.entity.Car;
import com.insurance.mgmt.entity.Customer;
import com.insurance.mgmt.repository.ICarRepository;
import com.insurance.mgmt.repository.ICustomerRepository;
import com.insurance.mgmt.service.CarService;
import com.insurance.mgmt.service.CustomerService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class InsuranceManagementApplicationTests {

	@Autowired
	private CustomerService customerService;
	
	@MockBean
	private ICustomerRepository customerRepository;
	
	@Autowired
	private CarService carService;
	
	@MockBean
	private ICarRepository carRepository;
	
	private List<Car> dummyCars;
	
	@Before
    public void setUp() {

        // Ortak veriler burada oluşturulmaktadır
        dummyCars = Stream.of(
                new Car(34, "vs", 1234, "Car", "Private", "Audi", "Petrol", 2, 5, 1, 6000, "Accepted", 1, 30, "17/05/2023 13:27:29", 0, 0, "17/06/2023 13:27:30"),
                new Car(34, "vs", 5678, "Car", "Private", "Audi", "Petrol", 2, 5, 1, 8000, "Accepted", 1, 30, "17/05/2023 13:00:00", 0, 0, "17/06/2023 13:00:00")
        ).collect(Collectors.toList());
    }

	@Test
	void contextLoads() {
	}

	@Test
	public void calculateTest() {
		Car car = new Car();
//	    car.setCustomer_id(12);		
//		Customer customer = new Customer();
//		customer.setCustomer_id(1);		
//		customer.setBirth("2002-06-03");
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
		assertEquals(6050, result1);
	}
	
	// CustomerService Sınıfındaki Metotların Testleri
	@Test
	public void getAllCustomerTest() {
		when(customerRepository.findAll()).thenReturn(Stream
				.of(new Customer(1, "1234567890", "2000-01-01", "x@gmail.com", "Eda", "Mutlu", "İstanbul", "Ataşehir", 1), 
						(new Customer(2, "1234567891", "2000-01-01", "xz@gmail.com", "Nur", "Mutlu", "İstanbul", "Ataşehir", 1))).collect(Collectors.toList()));
		assertEquals(2, customerService.getAllCustomer().size());
	}
	
	@Test
    public void testGetCustomerById() {
        int customerId = 1;
        Customer dummyCustomer = new Customer(1, "1234567890", "2000-01-01", "x@gmail.com", "Eda", "Mutlu", "İstanbul", "Ataşehir", 1);

        // customerRepository.findById(customerId) metodu çağrıldığında dummyCustomer nesnesini döndürmesini sağlanmaktadır
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(dummyCustomer));

        Customer result = customerService.getCustomerById(customerId);
        verify(customerRepository, times(1)).findById(customerId);
        assertEquals(dummyCustomer, result);
    }
	
	@Test
	public void saveCustomerTest() {
		 Customer customer = new Customer(1, "1234567890", "2000-01-01", "x@gmail.com", "Eda", "Mutlu", "İstanbul", "Ataşehir", 1);
		 customerService.save(customer);
		 verify(customerRepository, times(1)).save(customer);
	}
	
	@Test
    public void deleteByIdTest() {
		int customerId = 1;
        customerService.deleteById(customerId);
        verify(customerRepository, times(1)).deleteById(customerId);
    }
	
	// CarService Sınıfındaki Metotların Testleri
	@Test
	public void saveCarTest() {
		 Car car = new Car(34, "vs", 1234, "Car", "Private", "Audi", "Petrol", 2, 5, 1, 6000, "Accepted", 1, 30, "17/05/2023 13:27:29", 0, 0, "17/06/2023 13:27:30");
		 carService.save(car);
		 verify(carRepository, times(1)).save(car);
	}
	
	// Yöntem 1
	@Test
	public void getAllCarsTest() {
		when(carRepository.findAll()).thenReturn(Stream
				.of(new Car(34, "vs", 1234, "Car", "Private", "Audi", "Petrol", 2, 5, 1, 6000, "Accepted", 1, 30, "17/05/2023 13:27:29", 0, 0, "17/06/2023 13:27:30"), 
						(new Car(34, "vs", 5678, "Car", "Private", "Audi", "Petrol", 2, 5, 1, 8000, "Accepted", 1, 30, "17/05/2023 13:00:00", 0, 0, "17/06/2023 13:00:00"))).collect(Collectors.toList()));
		assertEquals(2, carService.getAllCars().size());
	}
	// Yöntem 2
	@Test
    public void testGetAllCars() {
        when(carRepository.findAll()).thenReturn(dummyCars);

        List<Car> result = carService.getAllCars();
        verify(carRepository, times(1)).findAll();
        assertEquals(dummyCars, result);
    }

    @Test
    public void testDeleteById() {
        int carId = 1;
        carService.deleteById(carId);
        verify(carRepository, times(1)).deleteById(carId);
    }

    @Test
    public void testGetCarId() {
        int carId = 1;
        Car dummyCar = new Car();

        when(carRepository.findById(carId)).thenReturn(Optional.of(dummyCar));
        Car result = carService.getCarId(carId);
        verify(carRepository, times(1)).findById(carId);
        assertEquals(dummyCar, result);
    }
	

}
