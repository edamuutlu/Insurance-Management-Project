package com.insurance.mgmt.InsuranceManagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.insurance.mgmt.entity.Kdv;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KdvValidationTest {

    private Validator validator;
    private Kdv kdv;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.byDefaultProvider().configure().buildValidatorFactory();
        validator = factory.getValidator();
        kdv = new Kdv();
        kdv.setProductType(1);
        kdv.setKdvRate(18);
    }

    @Test
    public void testValidKdv() {
        assertTrue(isKdvValid(kdv));
    }

    @Test
    public void testKdvRateNotNull() {
        kdv.setKdvRate(null); // Invalid value

        assertFalse(isKdvValid(kdv));
    }

    @Test
    public void testKdvRateMinValue() {
        kdv.setKdvRate(-5); // Invalid value

        assertFalse(isKdvValid(kdv));
    }

    @Test
    public void testKdvRateMaxValue() {
        kdv.setKdvRate(120); // Invalid value

        assertFalse(isKdvValid(kdv));
    }

    private boolean isKdvValid(Kdv kdv) {
        return validator.validate(kdv).isEmpty();
    }
}


