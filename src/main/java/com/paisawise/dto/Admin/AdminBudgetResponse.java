package com.paisawise.dto.Admin;

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
public class AdminBudgetResponse {

    private Long id;
    private String userName;
    private String userEmail;
    private Category category;
    private BigDecimal amount;
    private int month;
    private int year;

}
