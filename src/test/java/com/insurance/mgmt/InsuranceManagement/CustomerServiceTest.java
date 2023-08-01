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
import com.insurance.mgmt.entity.Customer;
import com.insurance.mgmt.service.CustomerService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class CustomerServiceTest {
	
	@MockBean
	private CustomerService customerService;
	
	private List<Customer> dummyCustomers = Stream.of(
            new Customer(1, "1234567890", "2000-01-01", "x@gmail.com", "Eda", "Mutlu", "İstanbul", "Ataşehir", 1),
            new Customer(2, "1234567891", "2000-01-01", "xz@gmail.com", "Nur", "Mutlu", "İstanbul", "Ataşehir", 1)
    ).collect(Collectors.toList());
	
	private Customer dummyCustomer = new Customer(1, "1234567890", "2000-01-01", "x@gmail.com", "Eda", "Mutlu", "İstanbul", "Ataşehir", 1);

	// CustomerService Sınıfındaki Metotların Testleri
		@Test
		public void getAllCustomerTest() {
			when(customerService.getAllCustomer()).thenReturn(dummyCustomers);
	        List<Customer> result = customerService.getAllCustomer();
	        assertEquals(dummyCustomers.size(), result.size());
	        assertEquals(dummyCustomers.get(0).getCustomerId(), result.get(0).getCustomerId());
	        assertEquals(dummyCustomers.get(1).getFirstname(), result.get(1).getFirstname());
		}
		
		@Test
	    public void testGetCustomerById() {
	        int customerId = 1;

	        // customerRepository.findById(customerId) metodu çağrıldığında dummyCustomer nesnesini döndürmesini sağlanmaktadır
	        when(customerService.getCustomerById(customerId)).thenReturn(dummyCustomer);

	        Customer result = customerService.getCustomerById(customerId);
	        verify(customerService, times(1)).getCustomerById(customerId);
	        assertEquals(dummyCustomer, result);
	    }
		
		@Test
		public void saveCustomerTest() {
			 customerService.save(dummyCustomer);
			 verify(customerService, times(1)).save(dummyCustomer);
		}
		
		@Test
	    public void deleteByIdTest() {
			int customerId = 1;
	        customerService.deleteById(customerId);
	        verify(customerService, times(1)).deleteById(customerId);
	    }
		
		@Test
	    public void findByStatusTest() {
	        when(customerService.findByStatus(1)).thenReturn(dummyCustomers);
	        List<Customer> result = customerService.findByStatus(1);
	        assertEquals(dummyCustomers, result);
	        verify(customerService, times(1)).findByStatus(1);
	    }
		
		@Test
	    public void findByStatusAndTc() {
	        when(customerService.findByStatusAndTc(1, "1234567890")).thenReturn(dummyCustomers);
	        List<Customer> result = customerService.findByStatusAndTc(1, "1234567890");
	        assertEquals(dummyCustomers, result);
	        verify(customerService, times(1)).findByStatusAndTc(1, "1234567890");
	    }
		
		@Test
	    public void findByStatusAndEmail() {
	        when(customerService.findByStatusAndEmail(1, "x@gmail.com")).thenReturn(dummyCustomers);
	        List<Customer> result = customerService.findByStatusAndEmail(1, "x@gmail.com");
	        assertEquals(dummyCustomers, result);
	        verify(customerService, times(1)).findByStatusAndEmail(1, "x@gmail.com");
	    }
}
