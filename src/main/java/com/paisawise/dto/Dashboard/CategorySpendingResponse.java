package com.paisawise.dto.Dashboard;

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
public class CategorySpendingResponse {

    private Category category;
    private BigDecimal amount;

}