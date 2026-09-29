package com.paisawise.dto.FinancialInsights;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyInsightResponse {

    private String month;
    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal savings;
}
