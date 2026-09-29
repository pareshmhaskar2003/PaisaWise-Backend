package com.paisawise.dto.Budget;

import com.paisawise.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetInsightResponse {

    private Long budgetId;

    private Category category;

    private Integer month;

    private Integer year;

    private BigDecimal budgetAmount;

    private BigDecimal spentAmount;

    private BigDecimal remainingAmount;

    private BigDecimal percentageUsed;

    private boolean overBudget;
}
