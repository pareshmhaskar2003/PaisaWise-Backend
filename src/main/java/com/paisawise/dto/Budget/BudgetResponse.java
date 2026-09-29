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
public class BudgetResponse {

    private Long id;
    private Category category;
    private BigDecimal amount;
    private Integer month;
    private Integer year;

}
