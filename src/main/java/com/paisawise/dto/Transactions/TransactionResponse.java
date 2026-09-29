package com.paisawise.dto.Transactions;

import com.paisawise.enums.Category;
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
public class TransactionResponse {

    private Long id;
    private BigDecimal amount;
    private String description;
    private Category category;
    private TransactionType type;
    private LocalDate transactionDate;

}