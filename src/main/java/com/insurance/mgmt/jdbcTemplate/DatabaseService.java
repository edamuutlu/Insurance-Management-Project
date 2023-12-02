package com.insurance.mgmt.jdbcTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void disableForeignKeyChecks() {
	    jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0");
	}

	public void enableForeignKeyChecks() {
	    jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1");
	}

}
