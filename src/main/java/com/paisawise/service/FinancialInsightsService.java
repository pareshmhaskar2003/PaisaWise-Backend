package com.paisawise.service;

import com.paisawise.dto.FinancialInsights.FinancialInsightsResponse;
import com.paisawise.dto.FinancialInsights.MonthlyInsightResponse;
import com.paisawise.entity.Transaction;
import com.paisawise.enums.TransactionType;
import com.paisawise.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class FinancialInsightsService {

    private final TransactionRepository transactionRepository;

    public FinancialInsightsResponse getInsights() {

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

        BigDecimal savings = totalIncome.subtract(totalExpense);

        BigDecimal savingsRate = BigDecimal.ZERO;

        if (totalIncome.compareTo(BigDecimal.ZERO) > 0) {
            savingsRate = savings
                    .divide(totalIncome, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        List<Transaction> transactions =
                transactionRepository.findByUserEmail(email);

        String highestSpendingCategory = null;
        BigDecimal highestSpendingAmount = BigDecimal.ZERO;

        var categoryTotals = new java.util.HashMap<String, BigDecimal>();

        for (Transaction transaction : transactions) {

            if (transaction.getType() != TransactionType.EXPENSE) {
                continue;
            }

            String category = transaction.getCategory().name();

            categoryTotals.put(
                    category,
                    categoryTotals.getOrDefault(
                            category,
                            BigDecimal.ZERO
                    ).add(transaction.getAmount())
            );
        }

        for (var entry : categoryTotals.entrySet()) {

            if (entry.getValue().compareTo(highestSpendingAmount) > 0) {
                highestSpendingCategory = entry.getKey();
                highestSpendingAmount = entry.getValue();
            }
        }

        List<MonthlyInsightResponse> monthlyData =
                buildMonthlyData(transactions);

        String financialAdvice;

        if (totalIncome.compareTo(BigDecimal.ZERO) == 0) {

            financialAdvice =
                    "Add your income and expenses to start receiving personalized financial insights.";

        } else if (savings.compareTo(BigDecimal.ZERO) < 0) {

            financialAdvice =
                    "Your expenses are higher than your income. Consider reducing unnecessary expenses.";

        } else if (savingsRate.compareTo(new BigDecimal("20")) < 0) {

            financialAdvice =
                    "Your savings rate is below 20%. Try to increase your monthly savings.";

        } else if (highestSpendingCategory != null) {

            financialAdvice =
                    "Your highest spending category is "
                            + highestSpendingCategory
                            + ". Consider setting a budget for this category.";

        } else {

            financialAdvice =
                    "Your finances look healthy. Keep tracking your income and expenses regularly.";
        }

        return new FinancialInsightsResponse(
                totalIncome,
                totalExpense,
                savings,
                savingsRate,
                highestSpendingCategory,
                highestSpendingAmount,
                monthlyData,
                financialAdvice
        );
    }

    private List<MonthlyInsightResponse> buildMonthlyData(
            List<Transaction> transactions
    ) {

        List<MonthlyInsightResponse> monthlyData =
                new ArrayList<>();

        LocalDate currentDate = LocalDate.now();

        for (int i = 5; i >= 0; i--) {

            LocalDate monthDate =
                    currentDate.minusMonths(i);

            int month = monthDate.getMonthValue();
            int year = monthDate.getYear();

            BigDecimal income = BigDecimal.ZERO;
            BigDecimal expense = BigDecimal.ZERO;

            for (Transaction transaction : transactions) {

                LocalDate transactionDate =
                        transaction.getTransactionDate();

                if (transactionDate.getMonthValue() != month
                        || transactionDate.getYear() != year) {
                    continue;
                }

                if (transaction.getType() == TransactionType.INCOME) {

                    income = income.add(transaction.getAmount());

                } else if (transaction.getType() == TransactionType.EXPENSE) {

                    expense = expense.add(transaction.getAmount());
                }
            }

            BigDecimal savings =
                    income.subtract(expense);

            String monthName =
                    monthDate.getMonth()
                            .getDisplayName(
                                    TextStyle.SHORT,
                                    Locale.ENGLISH
                            );

            monthlyData.add(
                    new MonthlyInsightResponse(
                            monthName,
                            income,
                            expense,
                            savings
                    )
            );
        }

        return monthlyData;
    }
}