package com.paisawise.dto.FinancialInsights;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialInsightsResponse {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal savings;
    private BigDecimal savingsRate;

    private String highestSpendingCategory;
    private BigDecimal highestSpendingAmount;

    private List<MonthlyInsightResponse> monthlyData;

    private String financialAdvice;
}