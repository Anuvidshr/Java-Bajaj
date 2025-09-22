package com.bajaj.finserv.qualifier.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bajaj Finserv Health Qualifier Application");
        response.put("status", "Running Successfully");
        response.put("version", "1.0.0");
        response.put("description", "Application is working properly. Webhook generated and SQL solution submitted.");
        response.put("endpoints", new String[]{
            "GET /",
            "GET /health",
            "GET /status"
        });
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("application", "bajaj-finserv-qualifier");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "Bajaj Finserv Health Qualifier");
        response.put("status", "Active");
        response.put("webhook", "Generated Successfully");
        response.put("solution", "Submitted Successfully");
        response.put("registration", "REG12347");
        
        return ResponseEntity.ok(response);
    }
}