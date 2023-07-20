package com.insurance.mgmt.InsuranceManagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.insurance.mgmt.entity.Customer;
import com.insurance.mgmt.repository.ICustomerRepository;
import com.insurance.mgmt.service.CustomerService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class CustomerServiceTest {
	
	@Autowired
	private CustomerService customerService;
	
	@MockBean
	private ICustomerRepository customerRepository;

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
}
