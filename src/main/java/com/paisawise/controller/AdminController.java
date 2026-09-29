package com.paisawise.controller;

import com.paisawise.dto.Admin.AdminBudgetResponse;
import com.paisawise.dto.Admin.AdminDashboardResponse;
import com.paisawise.dto.Admin.AdminTransactionResponse;
import com.paisawise.dto.Admin.AdminUserResponse;
import com.paisawise.entity.User;
import com.paisawise.enums.TransactionType;
import com.paisawise.repository.BudgetRepository;
import com.paisawise.repository.TransactionRepository;
import com.paisawise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminController {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;

    @GetMapping("/users")
    public List<AdminUserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AdminUserResponse mapToResponse(User user) {

        return new AdminUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isEnabled()
        );
    }

    @PutMapping("/users/{id}/toggle")
    public AdminUserResponse toggleUser(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(!user.isEnabled());

        User updatedUser = userRepository.save(user);

        return mapToResponse(updatedUser);
    }

    @GetMapping("/transactions")
    public List<AdminTransactionResponse> getAllTransactions() {

        return transactionRepository.findAll()
                .stream()
                .map(transaction -> new AdminTransactionResponse(
                        transaction.getId(),
                        transaction.getUser().getEmail(),
                        transaction.getAmount(),
                        transaction.getDescription(),
                        transaction.getCategory(),
                        transaction.getType(),
                        transaction.getTransactionDate()
                ))
                .toList();
    }

    @GetMapping("/budgets")
    public List<AdminBudgetResponse> getAllBudgets() {

        return budgetRepository.findAll()
                .stream()
                .map(budget -> new AdminBudgetResponse(
                        budget.getId(),
                        budget.getUser().getName(),
                        budget.getUser().getEmail(),
                        budget.getCategory(),
                        budget.getAmount(),
                        budget.getMonth(),
                        budget.getYear()
                ))
                .toList();
    }

    @GetMapping("/dashboard")
    public AdminDashboardResponse getDashboard() {

        long totalUsers = userRepository.count();
        long totalTransactions = transactionRepository.count();
        long totalBudgets = budgetRepository.count();

        BigDecimal totalIncome =
                transactionRepository.sumByType(TransactionType.INCOME);

        BigDecimal totalExpense =
                transactionRepository.sumByType(TransactionType.EXPENSE);

        return new AdminDashboardResponse(
                totalUsers,
                totalTransactions,
                totalBudgets,
                totalIncome,
                totalExpense
        );
    }

}