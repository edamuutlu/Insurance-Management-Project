package com.insurance.mgmt.InsuranceManagement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.insurance.mgmt.entity.Health;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;

@SpringBootTest
public class HealthValidationTest {

	@Autowired
    private final Validator validator = new LocalValidatorFactoryBean();

    @Test
    public void testValidHealthObject() {
        Health health = new Health();
        health.setJob("Software Engineer");
        health.setForWho("Self");
        health.setSgk((byte) 1);
        health.setHeight("175");
        health.setWeight("70");
        health.setSmokingOrAlcohol((byte) 1);
        health.setPastOperation((byte) 0);

        Set<ConstraintViolation<Health>> violations = validator.validate(health);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidHealthObject() {
        Health health = new Health(); // An empty object
        health.setJob(""); // Empty job field to trigger @NotEmpty
        health.setForWho(""); // Empty forWho field to trigger @NotEmpty
        // Invalid value to trigger @NotEmpty for sgk
        health.setHeight("18"); // Invalid height to trigger @Length
        health.setWeight("300"); // Invalid weight to trigger @Length
        // Invalid value to trigger @NotEmpty for smokingOrAlcohol

        Set<ConstraintViolation<Health>> violations = validator.validate(health);
        assertEquals(5, violations.size()); // Expecting 4 validation violations
    }
}

