package com.bajaj.finserv.qualifier.service;

import com.bajaj.finserv.qualifier.dto.WebhookRequest;
import com.bajaj.finserv.qualifier.dto.WebhookResponse;
import com.bajaj.finserv.qualifier.dto.SolutionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class WebhookService {
    
    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);
    
    private static final String GENERATE_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
    private static final String SUBMIT_SOLUTION_URL = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private SqlSolverService sqlSolverService;
    
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("Application started. Initiating webhook generation...");
        try {
            processWebhookFlow();
        } catch (Exception e) {
            logger.error("Error in webhook flow: ", e);
        }
    }
    
    private void processWebhookFlow() {
        // Step 1: Generate webhook
        WebhookResponse webhookResponse = generateWebhook();
        
        if (webhookResponse != null) {
            logger.info("Webhook generated successfully. URL: {}", webhookResponse.getWebhook());
            
            // Step 2: Solve SQL problem based on regNo
            String finalQuery = sqlSolverService.solveSqlProblem("REG12347");
            
            // Step 3: Submit solution
            submitSolution(webhookResponse.getAccessToken(), finalQuery);
        }
    }
    
    private WebhookResponse generateWebhook() {
        try {
            WebhookRequest request = new WebhookRequest();
            request.setName("John Doe");
            request.setRegNo("REG12347");
            request.setEmail("john@example.com");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<WebhookRequest> entity = new HttpEntity<>(request, headers);
            
            logger.info("Sending webhook generation request...");
            ResponseEntity<WebhookResponse> response = restTemplate.postForEntity(
                GENERATE_WEBHOOK_URL, entity, WebhookResponse.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Webhook generated successfully");
                return response.getBody();
            } else {
                logger.error("Failed to generate webhook. Status: {}", response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error generating webhook: ", e);
            return null;
        }
    }
    
    private void submitSolution(String accessToken, String finalQuery) {
        try {
            SolutionRequest solutionRequest = new SolutionRequest();
            solutionRequest.setFinalQuery(finalQuery);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken);
            
            HttpEntity<SolutionRequest> entity = new HttpEntity<>(solutionRequest, headers);
            
            logger.info("Submitting solution...");
            ResponseEntity<String> response = restTemplate.postForEntity(
                SUBMIT_SOLUTION_URL, entity, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Solution submitted successfully. Response: {}", response.getBody());
            } else {
                logger.error("Failed to submit solution. Status: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error submitting solution: ", e);
        }
    }
}