package com.fruitstore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Test Controller để kiểm tra các endpoint
 * 
 * @author FruitStore Team
 */
@RestController
@RequestMapping("/api/public")
public class TestController {

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to FruitStore API!");
        response.put("status", "success");
        response.put("timestamp", LocalDateTime.now());
        response.put("version", "1.0");
        return response;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "FruitStore Backend");
        return response;
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "FruitStore");
        response.put("description", "Ứng dụng thương mại điện tử bán trái cây");
        response.put("framework", "Spring Boot 3");
        response.put("java_version", System.getProperty("java.version"));
        response.put("spring_version", org.springframework.core.SpringVersion.getVersion());
        return response;
    }
}
