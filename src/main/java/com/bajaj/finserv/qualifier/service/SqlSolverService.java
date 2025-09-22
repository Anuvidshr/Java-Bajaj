package com.bajaj.finserv.qualifier.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SqlSolverService {
    
    private static final Logger logger = LoggerFactory.getLogger(SqlSolverService.class);
    
    public String solveSqlProblem(String regNo) {
        logger.info("Solving SQL problem for regNo: {}", regNo);
        
        // Example SQL query - replace with actual logic based on requirements
        String finalQuery = "SELECT * FROM users WHERE registration_number = '" + regNo + "'";
        
        logger.info("Generated SQL query: {}", finalQuery);
        return finalQuery;
    }
}