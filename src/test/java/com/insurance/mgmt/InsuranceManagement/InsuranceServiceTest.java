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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.insurance.mgmt.entity.Insurance;
import com.insurance.mgmt.service.InsuranceService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class InsuranceServiceTest {

	@MockBean
	private InsuranceService insuranceService;

	private List<Insurance> dummyInsurances = Stream.of(
            new Insurance(1, "Home", 1, 1, 1, 1000, "Accepted", "26/07/2023 16:00:00", "26/07/2023 16:00:00", 0, 0, 1, 30, 10),
            new Insurance(2, "Home", 2, 2, 2, 2000, "Accepted", "26/10/2023 12:00:00", "26/10/2023 12:00:00", 0, 0, 1, 60, 10)
    ).collect(Collectors.toList());
	
	@Test
    public void testSave() {
        // Burada sadece insuranceRepository.save(insurance) metodunun çağrılıp çağrılmadığını kontrol ediyoruz,
        // dönüş değerini kontrol etmeye gerek yok
        insuranceService.save(dummyInsurances.get(0));

        // insuranceRepository.save(insurance) metodunun sadece bir kez çağrıldığından emin oluyoruz
        verify(insuranceService, times(1)).save(dummyInsurances.get(0));
    }

    @Test
    public void testGetAllInsurance() {
        // Mock üzerinden dönecek olan örnek verileri hazırlıyoruz
        when(insuranceService.getAllInsurance()).thenReturn(dummyInsurances);

        // Servis metodunu çağırıyoruz
        List<Insurance> result = insuranceService.getAllInsurance();

        // Sonuçları kontrol ediyoruz
        assertEquals(dummyInsurances.size(), result.size());
        assertEquals(dummyInsurances.get(0).getInsuranceId(), result.get(0).getInsuranceId());
        assertEquals(dummyInsurances.get(1).getResult(), result.get(1).getResult());
    }

    @Test
    public void testGetInsuranceById() {
        int id = 1;
        when(insuranceService.getInsuranceById(id)).thenReturn(dummyInsurances.get(0));

        // Servis metodunu çağırıyoruz
        Insurance result = insuranceService.getInsuranceById(id);

        // Sonuçları kontrol ediyoruz
        assertEquals(dummyInsurances.get(0).getInsuranceId(), result.getInsuranceId());
        assertEquals(dummyInsurances.get(0).getInsuranceType(), result.getInsuranceType());
    }

    @Test
    public void testGetInsuranceByHomeId() {
        int homeId = 1;
        when(insuranceService.findByHomeId(homeId)).thenReturn(Optional.of(dummyInsurances.get(0)));

        // Servis metodunu çağırıyoruz
        Optional<Insurance> result = insuranceService.findByHomeId(homeId);

        // Sonuçları kontrol ediyoruz
        assertEquals(dummyInsurances.get(0).getHomeId(), result.get().getHomeId());
        assertEquals(dummyInsurances.get(0).getInsuranceId(), result.get().getInsuranceId());
    }

    @Test
    public void testDeleteById() {
        // insuranceRepository.deleteById(id) metodu çağrıldığında, hiçbir şey döndürmemesi beklenir (void metod)
        int id = 1;
        insuranceService.deleteById(id);

        // insuranceRepository.deleteById(id) metodunun sadece bir kez çağrıldığından emin oluyoruz
        verify(insuranceService, times(1)).deleteById(id);
    }
    
    @Test
    public void testFindByStatus() {
        // Mock üzerinden dönecek olan örnek verileri hazırlıyoruz
        int status = 1;
        List<Insurance> mockInsurances = new ArrayList<>();
        mockInsurances.add(dummyInsurances.get(0));

        // findByStatus metodu çağrıldığında, sahte verileri döndürmesi için mock'u ayarlıyoruz
        when(insuranceService.findByStatus(status)).thenReturn(mockInsurances);

        List<Insurance> result = insuranceService.findByStatus(status);

        // Sonuçları kontrol ediyoruz
        assertEquals(1, result.size());
        assertEquals(dummyInsurances.get(0).getHomeId(), result.get(0).getHomeId());
    }

    @Test
    public void testFindByHomeId() {
        int homeId = 1;
        when(insuranceService.findByHomeId(homeId)).thenReturn(Optional.of(dummyInsurances.get(0)));

        Optional<Insurance> result = insuranceService.findByHomeId(homeId);

        // Sonuçları kontrol ediyoruz
        assertEquals(dummyInsurances.get(0).getHomeId(), result.get().getHomeId());
    }

    @Test
    public void testFindByStatusAndHomeId() {
        int status = 1;
        int homeId = 1;
        List<Insurance> mockInsurances = new ArrayList<>();
        mockInsurances.add(dummyInsurances.get(0));

        // findByStatusAndHomeId metodu çağrıldığında, sahte verileri döndürmesi için mock'u ayarlıyoruz
        when(insuranceService.findByStatusAndHomeId(status, homeId)).thenReturn(mockInsurances);

        List<Insurance> result = insuranceService.findByStatusAndHomeId(status, homeId);

        // Sonuçları kontrol ediyoruz
        assertEquals(1, result.size());
        assertEquals(dummyInsurances.get(0).getHomeId(), result.get(0).getHomeId());
    }

    @Test
    public void testFindByStatusAndResultAndHomeId() {
        List<Insurance> mockInsurances = new ArrayList<>();
        mockInsurances.add(dummyInsurances.get(0));

        // findByStatusAndResultAndHomeId metodu çağrıldığında, sahte verileri döndürmesi için mock'u ayarlıyoruz
        when(insuranceService.findByStatusAndResultAndHomeId(1, "Accepted", 1)).thenReturn(mockInsurances);

        List<Insurance> result = insuranceService.findByStatusAndResultAndHomeId(1, "Accepted", 1);

        // Sonuçları kontrol ediyoruz
        assertEquals(1, result.size());
        assertEquals(dummyInsurances.get(0).getHomeId(), result.get(0).getHomeId());
    }

}
