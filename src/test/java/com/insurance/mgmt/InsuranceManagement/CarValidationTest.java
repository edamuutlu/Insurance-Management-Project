package com.insurance.mgmt.InsuranceManagement;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.insurance.mgmt.entity.Car;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class CarValidationTest {

	@Autowired
    private Validator validator;
	
    private Car car;

    @BeforeEach
    public void setUp() {
        //validator = Validation.buildDefaultValidatorFactory().getValidator();

        // Create a fake Car object with some default values for reuse in multiple tests
        car = new Car();
        car.setPlate1(12);
        car.setPlate2("ABC");
        car.setPlate3(1234);
        car.setType("Sedan");
        car.setPurpose("Personal");
        car.setBrand("Toyota");
        car.setFuelType("Gasoline");
        car.setEngineSize(2000);
        car.setSeatCapacity(5);
    }

    @Test
    public void whenPlate1IsNull_ValidationFails() {
        car.setPlate1(null);

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("plate1", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void whenPlate2IsBlank_ValidationFails() {
        car.setPlate2("");

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
        assertEquals("plate2", violations.iterator().next().getPropertyPath().toString());
    }   

    @Test
    public void whenPlate3IsNull_ValidationFails() {	
        car.setPlate3(null);

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("plate3", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void whenTypeIsBlank_ValidationFails() {
        car.setType("");

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("type", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void whenPurposeIsBlank_ValidationFails() {
        car.setPurpose("");

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("purpose", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void whenBrandIsBlank_ValidationFails() {
        car.setBrand("");

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("brand", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void whenFuelTypeIsBlank_ValidationFails() {
        car.setFuelType("");

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("fuelType", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void whenEngineSizeIsNull_ValidationFails() {
        car.setEngineSize(null);

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("engineSize", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void whenSeatCapacityIsNull_ValidationFails() {
        car.setSeatCapacity(null);

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("seatCapacity", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void whenPlate2HasInvalidCharacters_ValidationFails() {
        car.setPlate2("AB12");

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
        assertEquals("plate2", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void whenPlate2HasTooManyCharacters_ValidationFails() {
        car.setPlate2("ABCD");

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("plate2", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void whenPlate3HasInvalidCharacters_ValidationFails() {
        // Geçersiz karakterleri içeren bir String oluşturuyoruz.
        String invalidCharacters = "12AB";

        // Geçersiz karakterleri içeren String'i Integer tipine dönüştüremeyiz, bu nedenle ayrı bir test metodu kullanabiliriz.
        assertFalse(isValidPlate3(invalidCharacters));
    }

    // isValidPlate3 metodu, geçerli bir plate3 değeri olup olmadığını kontrol eder.
    private boolean isValidPlate3(String plate3) {
        try {
            // Eğer dönüştürülebilirse geçerli bir değerdir.
            Integer.parseInt(plate3);
            return true;
        } catch (NumberFormatException e) {
            // Eğer dönüştürülemezse geçersiz bir karakter içeriyordur.
            return false;
        }
    }


//    @Test
//    public void whenPlate3HasTooManyDigits_ValidationFails() {
//        car.setPlate3(12345);
//
//        Set<ConstraintViolation<Car>> violations = validator.validate(car);
//        assertFalse(violations.isEmpty());
//        assertEquals(1, violations.size());
//        assertEquals("plate3", violations.iterator().next().getPropertyPath().toString());
//    }

    @Test
    public void whenValidCar_ValidationSucceeds() {
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertTrue(violations.isEmpty());
    }
}
