package com.paisawise.service;

import com.paisawise.dto.Budget.BudgetInsightResponse;
import com.paisawise.dto.Notification.FinancialAlertResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialAlertService {

    private final BudgetInsightService budgetInsightService;

    public List<FinancialAlertResponse> getAlerts() {

        List<FinancialAlertResponse> alerts =
                new ArrayList<>();

        List<BudgetInsightResponse> budgets =
                budgetInsightService.getBudgetInsights();

        for (BudgetInsightResponse budget : budgets) {

            if (budget.isOverBudget()) {

                alerts.add(
                        new FinancialAlertResponse(
                                "BUDGET_EXCEEDED",
                                "Budget Exceeded",
                                "You have exceeded your "
                                        + budget.getCategory()
                                        + " budget.",
                                "DANGER"
                        )
                );

            } else if (budget.getPercentageUsed()
                    .compareTo(new java.math.BigDecimal("80")) >= 0) {

                alerts.add(
                        new FinancialAlertResponse(
                                "BUDGET_WARNING",
                                "Budget Almost Reached",
                                "You have used "
                                        + budget.getPercentageUsed()
                                        + "% of your "
                                        + budget.getCategory()
                                        + " budget.",
                                "WARNING"
                        )
                );
            }
        }

        return alerts;
    }
}
