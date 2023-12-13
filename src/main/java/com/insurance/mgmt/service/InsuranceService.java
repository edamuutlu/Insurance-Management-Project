package com.insurance.mgmt.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class InsuranceService {
	private JdbcTemplate jdbcTemplate;

    public InsuranceService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
