package com.kefawatch.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsApplication.class, args);
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return Map.of(
            "activeUsers", 145,
            "totalWatchTimeHours", 8500,
            "status", "Healthy"
        );
    }
}
