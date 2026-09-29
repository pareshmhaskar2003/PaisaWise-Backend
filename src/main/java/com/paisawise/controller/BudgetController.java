package com.paisawise.controller;

import com.paisawise.dto.Budget.BudgetResponse;
import com.paisawise.dto.Budget.CreateBudgetRequest;
import com.paisawise.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(
            @RequestBody @Valid CreateBudgetRequest request) {

        BudgetResponse budget = budgetService.createBudget(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(budget);
    }

    @GetMapping("/my")
    public ResponseEntity<List<BudgetResponse>> getMyBudgets() {

        return ResponseEntity.ok(
                budgetService.getMyBudgets()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> updateBudget(
            @PathVariable Long id,
            @RequestBody @Valid CreateBudgetRequest request) {

        BudgetResponse budget = budgetService.updateBudget(id, request);

        return ResponseEntity.ok(budget);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {

        budgetService.deleteBudget(id);

        return ResponseEntity.noContent().build();
    }

}