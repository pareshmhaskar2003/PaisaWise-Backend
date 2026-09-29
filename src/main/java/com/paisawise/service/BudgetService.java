package com.paisawise.service;

import com.paisawise.dto.Budget.BudgetResponse;
import com.paisawise.dto.Budget.CreateBudgetRequest;
import com.paisawise.entity.Budget;
import com.paisawise.entity.User;
import com.paisawise.exception.ResourceNotFoundException;
import com.paisawise.repository.BudgetRepository;
import com.paisawise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    public BudgetResponse createBudget(CreateBudgetRequest request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Budget budget = new Budget();

        budget.setUser(user);
        budget.setCategory(request.getCategory());
        budget.setAmount(request.getAmount());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());

        Budget savedBudget = budgetRepository.save(budget);

        return new BudgetResponse(
                savedBudget.getId(),
                savedBudget.getCategory(),
                savedBudget.getAmount(),
                savedBudget.getMonth(),
                savedBudget.getYear()
        );
    }

    public List<BudgetResponse> getMyBudgets() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return budgetRepository.findByUserId(
                        userRepository.findByEmail(email)
                                .orElseThrow(() ->
                                        new ResourceNotFoundException("User not found"))
                                .getId()
                ).stream()
                .map(budget -> new BudgetResponse(
                        budget.getId(),
                        budget.getCategory(),
                        budget.getAmount(),
                        budget.getMonth(),
                        budget.getYear()
                ))
                .toList();
    }

    public BudgetResponse updateBudget(
            Long id,
            CreateBudgetRequest request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Budget not found"));

        if (!budget.getUser().getEmail().equals(email)) {
            throw new ResourceNotFoundException("Budget not found");
        }

        budget.setCategory(request.getCategory());
        budget.setAmount(request.getAmount());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());

        Budget savedBudget = budgetRepository.save(budget);

        return new BudgetResponse(
                savedBudget.getId(),
                savedBudget.getCategory(),
                savedBudget.getAmount(),
                savedBudget.getMonth(),
                savedBudget.getYear()
        );
    }

    public void deleteBudget(Long id) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Budget not found"));

        if (!budget.getUser().getEmail().equals(email)) {
            throw new ResourceNotFoundException("Budget not found");
        }

        budgetRepository.delete(budget);
    }

}