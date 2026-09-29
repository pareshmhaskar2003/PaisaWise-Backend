package com.paisawise.dto.Transactions;

import com.paisawise.enums.Category;
import com.paisawise.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class CreateTransactionRequest {

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotBlank
    private String description;

    @NotNull
    private Category category;

    @NotNull
    private TransactionType type;

    @NotNull
    private LocalDate transactionDate;

}