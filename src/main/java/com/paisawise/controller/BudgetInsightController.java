package com.paisawise.controller;

import com.paisawise.dto.Budget.BudgetInsightResponse;
import com.paisawise.service.BudgetInsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/budget-insights")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BudgetInsightController {

    private final BudgetInsightService budgetInsightService;

    @GetMapping
    public ResponseEntity<List<BudgetInsightResponse>> getBudgetInsights() {

        return ResponseEntity.ok(
                budgetInsightService.getBudgetInsights()
        );
    }
}