package com.insurance.mgmt.InsuranceManagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.insurance.mgmt.entity.Health;
import com.insurance.mgmt.repository.IHealthRepository;
import com.insurance.mgmt.service.HealthService;
import com.insurance.mgmt.util.CalculateMethods;

@SpringBootTest
class HealthServiceTest {

	@Autowired
	private HealthService healthService;

	@MockBean
	private IHealthRepository healthRepository;

	private List<Health> dummyHealths = Stream
			.of(new Health(1, 1, "Job 1", "Person 1", (byte) 0, "180", "75", (byte) 0, (byte) 0, 1, 6, "12/12/2023 12:00:00"),
					new Health(2, 1, "Job 2", "Person 2", (byte) 0, "170", "60", (byte) 1, (byte) 1, 2, 6, "01/01/2023 12:00:00"))
			.collect(Collectors.toList());

	@Test
	public void testCalculateHealthInsurance() {
		Health health = new Health(1, 1, "Askeri Personel", "Me", (byte) 1, "200", "80", (byte) 1, (byte) 0, 1, 6, "01/01/2023 12:00:00");
		int kdvRate = 18;
		int riskFactor = 3;

		double baseOffer = 500 + 400 + 200 + 200 + 400 + 400 + 400 + 120; // KDV hariç beklenen teklif
        double expectedOffer = baseOffer + ((baseOffer * kdvRate) / 100); // KDV dahil beklenen teklif

		double calculatedOffer = CalculateMethods.calculateHealthInsurance(health, kdvRate, riskFactor);

		assertEquals(expectedOffer, calculatedOffer, 0.001); // 0.001 toleransla doğrulama
	}

	@Test
    public void testGetAllHealths() {
		when(healthRepository.findAll()).thenReturn(dummyHealths);

        List<Health> result = healthService.getAllHealths();

        assertEquals(2, result.size());
        assertEquals("Job 1", result.get(0).getJob());
        assertEquals("Job 2", result.get(1).getJob());
    }

	@Test
    public void testGetHealthById() {
        when(healthRepository.findById(1)).thenReturn(Optional.of(dummyHealths.get(0)));

        Health result = healthService.getHealthById(1);

        assertNotNull(result);
        assertEquals("Job 1", result.getJob());
    }

	@Test
    public void testFindByStatusAndCustomerId() {
        when(healthRepository.findByStatusAndCustomerId(1, 1)).thenReturn(dummyHealths);

        List<Health> result = healthService.findByStatusAndCustomerId(1, 1);

        assertEquals(2, result.size());
        assertEquals("Person 1", result.get(0).getForWho());
        assertEquals("Person 2", result.get(1).getForWho());
    }

	@Test
	public void testSaveHealth() {
		Health newHealth = new Health(3, 2, "Job 3", "Person 3", (byte) 0, "160cm", "55kg", (byte) 0, (byte) 0, 1, 6, "01/01/2023 12:00:00");

		when(healthRepository.save(newHealth)).thenReturn(newHealth);

		healthService.save(newHealth);

		verify(healthRepository, times(1)).save(newHealth);
	}

	@Test
	public void testDeleteHealthById() {
		doNothing().when(healthRepository).deleteById(1);

		healthService.deleteById(1);

		verify(healthRepository, times(1)).deleteById(1);
	}

}
