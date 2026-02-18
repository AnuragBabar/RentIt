package com.booking.logging;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CentralLogger {

    private final RestTemplate restTemplate;

    @Value("${logging.service.url}")
    private String loggingServiceUrl;

    public CentralLogger(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void log(String message) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("message", message);

            restTemplate.postForObject(
                    loggingServiceUrl + "/api/logs",
                    body,
                    Void.class);
        } catch (Exception e) {
            System.err.println("Failed to send log to logging service: " + e.getMessage());
        }
    }
}
