package com.insurance.mgmt.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.insurance.mgmt.entity.Health;

@Service
public class InsuranceService {
	private JdbcTemplate jdbcTemplate;

    public InsuranceService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public int saveHealthInsurance(Health health, int kdvRate, double offer) {
        String sql = "INSERT INTO health_insurance (insurance_type, customer_id, health_id, company_id, start_date, end_date, days_diff, refund, kdv, period, offer, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
		LocalDateTime endDate = now.plusDays(health.getPeriod());

        jdbcTemplate.update(sql, "Health", health.getCustomerId(), health.getHealthId(), 0,
        		now.format(formatter), endDate.format(formatter), 0, 0, kdvRate, health.getPeriod(),
                offer, 1);
        
     // Son eklenen kimlik değerini almak için
       int lastInsertedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", int.class); 
       return lastInsertedId;
    }


    public void updateData(int kdv, int productType) {
        String sql = "UPDATE kdv SET kdv_rate = ? WHERE product_type = ?";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setInt(1, kdv);
            preparedStatement.setInt(2, productType);
        });
    }
    
    public void deleteCustomerData(int customerId) {
        String sql = "DELETE FROM customer WHERE customer_id = ?";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setInt(1, customerId);
        });
    }

    public void deleteCarData(int customerId) {
        String sql = "DELETE FROM car WHERE customer_id = ?";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setInt(1, customerId);
        });
    }
    
    public void deleteHomeData(int customerId) {
        String sql = "DELETE FROM home WHERE customer_id = ?";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setInt(1, customerId);
        });
    }
    
    public void deleteHealthData(int customerId) {
        String sql = "DELETE FROM health WHERE customer_id = ?";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setInt(1, customerId);
        });
    }
    
    public void deleteCarInsuranceData(int customerId) {
        String sql = "DELETE FROM car_insurance WHERE customer_id = ?";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setInt(1, customerId);
        });
    }
    
    public void deleteHomeInsuranceData(int customerId) {
        String sql = "DELETE FROM home_insurance WHERE customer_id = ?";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setInt(1, customerId);
        });
    }
    
    public void deleteHealthInsuranceData(int customerId) {
        String sql = "DELETE FROM health_insurance WHERE customer_id = ?";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setInt(1, customerId);
        });
    }

}
