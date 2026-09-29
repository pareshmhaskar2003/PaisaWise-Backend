package com.paisawise.dto.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;

    private List<CategorySpendingResponse> spendingByCategory;

    private String highestSpendingCategory;
    private BigDecimal highestSpendingAmount;

    private String financialAdvice;

    private List<RecentTransactionResponse> recentTransactions;
}