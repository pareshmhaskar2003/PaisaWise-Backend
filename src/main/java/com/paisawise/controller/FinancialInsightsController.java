package com.paisawise.controller;

import com.paisawise.dto.FinancialInsights.FinancialInsightsResponse;
import com.paisawise.service.FinancialInsightsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/financial-insights")
@RequiredArgsConstructor
@CrossOrigin("*")
public class FinancialInsightsController {

    private final FinancialInsightsService financialInsightsService;

    @GetMapping
    public ResponseEntity<FinancialInsightsResponse> getInsights() {

        return ResponseEntity.ok(
                financialInsightsService.getInsights()
        );
    }
}
