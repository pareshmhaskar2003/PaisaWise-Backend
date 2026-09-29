package com.paisawise.repository;

import com.paisawise.dto.Dashboard.CategorySpendingResponse;
import com.paisawise.entity.Transaction;
import com.paisawise.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByUserEmail(String email);

    List<Transaction> findTop5ByUserEmailOrderByTransactionDateDesc(String email);

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.user.email = :email
            AND t.type = :type
            """)
    BigDecimal sumByUserEmailAndType(
            @Param("email") String email,
            @Param("type") TransactionType type
    );

    @Query("""
        SELECT new com.paisawise.dto.Dashboard.CategorySpendingResponse(
            t.category,
            SUM(t.amount)
        )
        FROM Transaction t
        WHERE t.user.email = :email
        AND t.type = :type
        GROUP BY t.category
        ORDER BY SUM(t.amount) DESC
        """)
    List<CategorySpendingResponse> getSpendingByCategory(
            @Param("email") String email,
            @Param("type") TransactionType type
    );

    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.type = :type
        """)
    BigDecimal sumByType(
            @Param("type") TransactionType type
    );

}