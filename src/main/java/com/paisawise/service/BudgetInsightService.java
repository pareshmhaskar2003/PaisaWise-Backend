package com.paisawise.service;

import com.paisawise.dto.Budget.BudgetInsightResponse;
import com.paisawise.entity.Budget;
import com.paisawise.entity.Transaction;
import com.paisawise.entity.User;
import com.paisawise.enums.TransactionType;
import com.paisawise.repository.BudgetRepository;
import com.paisawise.repository.TransactionRepository;
import com.paisawise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetInsightService {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public List<BudgetInsightResponse> getBudgetInsights() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Long userId = user.getId();

        List<Budget> budgets =
                budgetRepository.findByUserId(userId);

        List<Transaction> transactions =
                transactionRepository.findByUserEmail(email);

        List<BudgetInsightResponse> insights =
                new ArrayList<>();

        for (Budget budget : budgets) {

            BigDecimal spentAmount = transactions.stream()
                    .filter(transaction ->
                            transaction.getType()
                                    == TransactionType.EXPENSE
                    )
                    .filter(transaction ->
                            transaction.getCategory()
                                    == budget.getCategory()
                    )
                    .filter(transaction ->
                            transaction.getTransactionDate()
                                    .getMonthValue()
                                    == budget.getMonth()
                    )
                    .filter(transaction ->
                            transaction.getTransactionDate()
                                    .getYear()
                                    == budget.getYear()
                    )
                    .map(Transaction::getAmount)
                    .reduce(
                            BigDecimal.ZERO,
                            BigDecimal::add
                    );

            BigDecimal budgetAmount =
                    budget.getAmount();

            BigDecimal remainingAmount =
                    budgetAmount.subtract(spentAmount);

            BigDecimal percentageUsed =
                    BigDecimal.ZERO;

            if (budgetAmount.compareTo(BigDecimal.ZERO) > 0) {

                percentageUsed = spentAmount
                        .divide(
                                budgetAmount,
                                4,
                                RoundingMode.HALF_UP
                        )
                        .multiply(new BigDecimal("100"))
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );
            }

            boolean overBudget =
                    spentAmount.compareTo(budgetAmount) > 0;

            insights.add(
                    new BudgetInsightResponse(
                            budget.getId(),
                            budget.getCategory(),
                            budget.getMonth(),
                            budget.getYear(),
                            budgetAmount,
                            spentAmount,
                            remainingAmount,
                            percentageUsed,
                            overBudget
                    )
            );
        }

        return insights;
    }
}
