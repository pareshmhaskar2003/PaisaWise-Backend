package com.paisawise.dto.Dashboard;

import com.paisawise.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentTransactionResponse {

    private Long id;
    private String description;
    private String category;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDate transactionDate;
}