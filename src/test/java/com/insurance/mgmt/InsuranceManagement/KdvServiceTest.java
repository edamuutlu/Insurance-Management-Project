package com.insurance.mgmt.InsuranceManagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.insurance.mgmt.entity.Kdv;
import com.insurance.mgmt.repository.IKdvRepository;
import com.insurance.mgmt.service.KdvService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class KdvServiceTest {

	@Autowired
	private KdvService kdvService;

	@MockBean
	private IKdvRepository kdvRepository;

	@Test
	public void testGetProductTypeById() {
		Kdv kdvMock = new Kdv();
		kdvMock.setProductType(1);
		when(kdvRepository.findById(1)).thenReturn(Optional.of(kdvMock));

		Kdv result = kdvService.getProductTypeById(1);

		assertNotNull(result);
		assertEquals(1, result.getProductType());
	}

	@Test
	public void testSave() {
		Kdv kdvMock = new Kdv();
		kdvMock.setProductType(2);

		kdvService.save(kdvMock);

		// Verify that kdvRepository.save method is called with the correct argument
		verify(kdvRepository, times(1)).save(kdvMock);
	}
}
