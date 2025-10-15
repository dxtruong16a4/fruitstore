package com.fruitstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Home Controller để xử lý root path và trang chủ
 * 
 * @author FruitStore Team
 */
@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "🍎 FruitStore");
        response.put("message", "Chào mừng đến với FruitStore - Ứng dụng thương mại điện tử bán trái cây!");
        response.put("status", "success");
        response.put("timestamp", LocalDateTime.now());
        response.put("version", "1.0");
        response.put("framework", "Spring Boot 3");
        response.put("endpoints", Map.of(
            "home", "/",
            "api_public", "/api/public/",
            "health", "/api/public/health",
            "info", "/api/public/info",
            "actuator", "/actuator/health"
        ));
        response.put("description", "Hệ thống thương mại điện tử quy mô nhỏ, phù hợp cho dự án học tập");
        
        return response;
    }
}
