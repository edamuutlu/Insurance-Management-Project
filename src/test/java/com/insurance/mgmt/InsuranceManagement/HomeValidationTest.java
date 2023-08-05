package com.insurance.mgmt.InsuranceManagement;

import com.insurance.mgmt.entity.Home;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeValidationTest {

    private Validator validator;
    private Home home;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        home = new Home();
        home.setCustomerId(12345);
        home.setBuildingType("Apartment");
        home.setTypeOfUse("Residential");
        home.setProvince("34");
        home.setDistrict("1852");
        home.setNeighbourhood("40739");
        home.setBuildingNumber(10);
        home.setApartment(3);
        home.setFloor("Ground Floor");
        home.setBuildingAge(2000);
        home.setInsurerTitle("Owner");
    }

    @Test
    public void testBuildingTypeNotBlank() {
        home.setBuildingType(""); // Invalid value

        assertFalse(isHomeValid(home));
    }

    @Test
    public void testTypeOfUseNotBlank() {
        home.setTypeOfUse("  "); // Invalid value

        assertFalse(isHomeValid(home));
    }

    @Test
    public void testProvinceNotBlank() {
        home.setProvince(null); // Invalid value

        assertFalse(isHomeValid(home));
    }

    @Test
    public void testDistrictNotBlank() {
        home.setDistrict(""); // Invalid value

        assertFalse(isHomeValid(home));
    }

    @Test
    public void testNeighbourhoodNotBlank() {
        home.setNeighbourhood("  "); // Invalid value

        assertFalse(isHomeValid(home));
    }

    @Test
    public void testFloorNotBlank() {
        home.setFloor(null); // Invalid value

        assertFalse(isHomeValid(home));
    }

//    @Test
//    public void testApartmentNonNegative() {
//        home.setApartment(-3); // Invalid value
//
//        assertFalse(isHomeValid(home));
//    }

    @Test
    public void testInsurerTitleNotBlank() {
        home.setInsurerTitle(""); // Invalid value

        assertFalse(isHomeValid(home));
    }

    @Test
    public void testValidHome() {
        assertTrue(isHomeValid(home));
    }

    private boolean isHomeValid(Home home) {
        Set<ConstraintViolation<Home>> violations = validator.validate(home);
        return violations.isEmpty();
    }
}


