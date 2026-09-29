package com.paisawise.service;

import com.paisawise.dto.Dashboard.CategorySpendingResponse;
import com.paisawise.dto.Dashboard.DashboardResponse;
import com.paisawise.dto.Dashboard.RecentTransactionResponse;
import com.paisawise.entity.Transaction;
import com.paisawise.enums.TransactionType;
import com.paisawise.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public DashboardResponse getDashboard() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        BigDecimal totalIncome =
                transactionRepository.sumByUserEmailAndType(
                        email,
                        TransactionType.INCOME
                );

        BigDecimal totalExpense =
                transactionRepository.sumByUserEmailAndType(
                        email,
                        TransactionType.EXPENSE
                );

        BigDecimal balance =
                totalIncome.subtract(totalExpense);

        List<CategorySpendingResponse> spendingByCategory =
                transactionRepository.getSpendingByCategory(
                        email,
                        TransactionType.EXPENSE
                );

        List<Transaction> recentTransactions =
                transactionRepository.findTop5ByUserEmailOrderByTransactionDateDesc(email);

        List<RecentTransactionResponse> recentTransactionResponses =
                recentTransactions.stream()
                        .map(transaction -> new RecentTransactionResponse(
                                transaction.getId(),
                                transaction.getDescription(),
                                transaction.getCategory().name(),
                                transaction.getAmount(),
                                transaction.getType(),
                                transaction.getTransactionDate()
                        ))
                        .toList();

        String highestSpendingCategory = null;
        BigDecimal highestSpendingAmount = BigDecimal.ZERO;

        if (!spendingByCategory.isEmpty()) {
            CategorySpendingResponse highest = spendingByCategory.get(0);

            highestSpendingCategory = highest.getCategory().name();
            highestSpendingAmount = highest.getAmount();
        }

        String financialAdvice;

        if (totalIncome.compareTo(BigDecimal.ZERO) == 0) {

            financialAdvice = "Start adding your income and expenses to get personalized insights.";

        } else if (totalExpense.compareTo(totalIncome) > 0) {

            financialAdvice = "Your expenses are higher than your income. Consider reducing unnecessary spending.";

        } else if (totalExpense.compareTo(totalIncome.multiply(new BigDecimal("0.80"))) > 0) {

            financialAdvice = "You are spending more than 80% of your income. Consider increasing your savings.";

        } else if (highestSpendingCategory != null) {

            financialAdvice = "Your highest spending category is "
                    + highestSpendingCategory
                    + ". Consider setting a budget for this category.";

        } else {

            financialAdvice = "Your spending looks healthy. Keep tracking your finances regularly.";
        }

        return new DashboardResponse(
                totalIncome,
                totalExpense,
                balance,
                spendingByCategory,
                highestSpendingCategory,
                highestSpendingAmount,
                financialAdvice,
                recentTransactionResponses
        );
    }
}