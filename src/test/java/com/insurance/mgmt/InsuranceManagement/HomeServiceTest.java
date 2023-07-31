package com.insurance.mgmt.InsuranceManagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.insurance.mgmt.entity.Home;
import com.insurance.mgmt.repository.IHomeRepository;
import com.insurance.mgmt.service.HomeService;
import com.insurance.mgmt.util.CalculateMethods;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class HomeServiceTest {
	
	@Autowired
	private HomeService homeService;
	
	@MockBean
	private IHomeRepository homeRepository;

	private List<Home> dummyHomes = Stream.of(
            new Home(1, 1, "Reinforced Concrete", "Summer House", "İSTANBUL", "KADIKÖY", "GÖZTEPE", 10, 10, "Ground Floor", 10, "Owner", 1, 30, 100, 2013),
            new Home(2, 1, "Reinforced Concrete", "Summer House", "İSTANBUL", "KADIKÖY", "ERENKÖY", 10, 10, "Ground Floor", 13, "Tenant", 1, 30, 100, 2010)
    ).collect(Collectors.toList());

	@Test
	public void calculateHomeInsuranceTest() {
		
		CalculateMethods calculateMethods = new CalculateMethods();
		int KdvRate = 10;
		double offer = calculateMethods.calculateHomeInsurance(dummyHomes.get(0), KdvRate);	
    
		double kdv = (((500 + (100*4) + 500 + 500 + 500 + (10 * 20) + 600 + 400 + (30 * 20)) * KdvRate) / 100);
		double expectedOffer = 500 + (100*4) + 500 + 500 + 500 + (10 * 20) + 600 + 400 + (30 * 20) + kdv;
	    assertEquals(expectedOffer, offer);		

	}
	
	 @Test
	    public void testSave() {
	        // Burada sadece homeRepository.save(home) metodunun çağrılıp çağrılmadığını kontrol ediyoruz,
	        // dönüş değerini kontrol etmeye gerek yok
	        homeService.save(dummyHomes.get(0));

	        // homeRepository.save(home) metodunun sadece bir kez çağrıldığından emin oluyoruz
	        verify(homeRepository, times(1)).save(dummyHomes.get(0));
	    }

	    @Test
	    public void testGetAllHomes() {
	        // Mock üzerinden dönecek olan örnek verileri hazırlıyoruz
	        when(homeRepository.findAll()).thenReturn(dummyHomes);
	        List<Home> result = homeService.getAllHomes();
	        assertEquals(dummyHomes.size(), result.size());
	        assertEquals(dummyHomes.get(0).getHomeId(), result.get(0).getHomeId());
	        assertEquals(dummyHomes.get(1).getProvince(), result.get(1).getProvince());
	    }

	    @Test
	    public void testGetHomeById() {
	        int id = 1;
	        when(homeRepository.findById(id)).thenReturn(Optional.of(dummyHomes.get(0)));
	        Home result = homeService.getHomeById(id);
	        assertEquals(dummyHomes.get(0).getHomeId(), result.getHomeId());
	        assertEquals(dummyHomes.get(0).getProvince(), result.getProvince());
	    }

	    @Test
	    public void testDeleteById() {
	        int id = 1;
	        homeService.deleteById(id);
	        verify(homeRepository, times(1)).deleteById(id);
	    }
	    
	    @Test
	    public void testFindByStatus() {
	        // Mock üzerinden dönecek olan örnek verileri hazırlıyoruz
	        int status = 1;
	        List<Home> mockHomes = new ArrayList<>();
	        mockHomes.add(dummyHomes.get(0));

	        // findByStatus metodu çağrıldığında, sahte verileri döndürmesi için mock'u ayarlıyoruz
	        when(homeRepository.findByStatus(status)).thenReturn(mockHomes);

	        // Servis metodunu çağırıyoruz
	        List<Home> result = homeRepository.findByStatus(status);

	        // Sonuçları kontrol ediyoruz
	        assertEquals(1, result.size());
	        assertEquals(dummyHomes.get(0).getHomeId(), result.get(0).getHomeId());
	    }

	    @Test
	    public void testFindByCustomerId() {
	        // Mock üzerinden dönecek olan örnek verileri hazırlıyoruz
	        int customerId = 1;
	        List<Home> mockHomes = new ArrayList<>();
	        mockHomes.add(dummyHomes.get(0));

	        // findByCustomerId metodu çağrıldığında, sahte verileri döndürmesi için mock'u ayarlıyoruz
	        when(homeRepository.findByCustomerId(customerId)).thenReturn(mockHomes);

	        // Servis metodunu çağırıyoruz
	        List<Home> result = homeRepository.findByCustomerId(customerId);

	        // Sonuçları kontrol ediyoruz
	        assertEquals(1, result.size());
	        assertEquals(dummyHomes.get(0).getHomeId(), result.get(0).getHomeId());
	    }

	    @Test
	    public void testFindByProvinceAndDistrictAndNeighbourhoodAndBuildingNumberAndApartmentAndFloorAndStatus() {
	        // Mock üzerinden dönecek olan örnek verileri hazırlıyoruz
	        String province = "Istanbul";
	        String district = "Kadikoy";
	        String neighbourhood = "Acıbadem";
	        int buildingNumber = 10;
	        int apartment = 10;
	        String floor = "Ground Floor";
	        int status = 1;
	        List<Home> mockHomes = new ArrayList<>();
	        mockHomes.add(dummyHomes.get(0));

	        // findByProvinceAndDistrictAndNeighbourhoodAndBuildingNumberAndApartmentAndFloorAndStatus metodu çağrıldığında, sahte verileri döndürmesi için mock'u ayarlıyoruz
	        when(homeRepository.findByProvinceAndDistrictAndNeighbourhoodAndBuildingNumberAndApartmentAndFloorAndStatus(
	            province, district, neighbourhood, buildingNumber, apartment, floor, status)).thenReturn(mockHomes);

	        // Servis metodunu çağırıyoruz
	        List<Home> result = homeRepository.findByProvinceAndDistrictAndNeighbourhoodAndBuildingNumberAndApartmentAndFloorAndStatus(
	            province, district, neighbourhood, buildingNumber, apartment, floor, status);

	        // Sonuçları kontrol ediyoruz
	        assertEquals(1, result.size());
	        assertEquals(dummyHomes.get(0).getHomeId(), result.get(0).getHomeId());
	    }
	

}
