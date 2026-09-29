package com.paisawise.service;

import com.paisawise.dto.Transactions.CreateTransactionRequest;
import com.paisawise.dto.Transactions.TransactionResponse;
import com.paisawise.entity.Transaction;
import com.paisawise.entity.User;
import com.paisawise.enums.TransactionType;
import com.paisawise.exception.InsufficientBalanceException;
import com.paisawise.exception.ResourceNotFoundException;
import com.paisawise.repository.TransactionRepository;
import com.paisawise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionResponse createTransaction(
            CreateTransactionRequest request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        validateExpenseBalance(email, request.getType(), request.getAmount());

        Transaction transaction = new Transaction();

        transaction.setUser(user);
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setCategory(request.getCategory());
        transaction.setType(request.getType());
        transaction.setTransactionDate(request.getTransactionDate());

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        return new TransactionResponse(
                savedTransaction.getId(),
                savedTransaction.getAmount(),
                savedTransaction.getDescription(),
                savedTransaction.getCategory(),
                savedTransaction.getType(),
                savedTransaction.getTransactionDate()
        );
    }

    public List<TransactionResponse> getAllTransactions() {

        return transactionRepository.findAll()
                .stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getId(),
                        transaction.getAmount(),
                        transaction.getDescription(),
                        transaction.getCategory(),
                        transaction.getType(),
                        transaction.getTransactionDate()
                ))
                .toList();
    }

    public List<TransactionResponse> getMyTransactions() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return transactionRepository.findByUserEmail(email)
                .stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getId(),
                        transaction.getAmount(),
                        transaction.getDescription(),
                        transaction.getCategory(),
                        transaction.getType(),
                        transaction.getTransactionDate()
                ))
                .toList();
    }

    public TransactionResponse updateTransaction(
            Long id,
            CreateTransactionRequest request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Transaction not found"));

        if (!transaction.getUser().getEmail().equals(email)) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        BigDecimal balanceExcludingCurrent = getBalance(email);
        if (transaction.getType() == TransactionType.INCOME) {
            balanceExcludingCurrent = balanceExcludingCurrent
                    .subtract(transaction.getAmount());
        } else {
            balanceExcludingCurrent = balanceExcludingCurrent
                    .add(transaction.getAmount());
        }

        BigDecimal resultingBalance = request.getType() == TransactionType.INCOME
                ? balanceExcludingCurrent.add(request.getAmount())
                : balanceExcludingCurrent.subtract(request.getAmount());

        validateResultingBalance(resultingBalance, request.getType());

        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setCategory(request.getCategory());
        transaction.setType(request.getType());
        transaction.setTransactionDate(request.getTransactionDate());

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        return new TransactionResponse(
                savedTransaction.getId(),
                savedTransaction.getAmount(),
                savedTransaction.getDescription(),
                savedTransaction.getCategory(),
                savedTransaction.getType(),
                savedTransaction.getTransactionDate()
        );
    }

    public void deleteTransaction(Long id) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Transaction not found"));

        if (!transaction.getUser().getEmail().equals(email)) {
            throw new ResourceNotFoundException("Transaction not found");
        }

                if (transaction.getType() == TransactionType.INCOME) {
                        BigDecimal resultingBalance = getBalance(email)
                                        .subtract(transaction.getAmount());
                        validateResultingBalance(resultingBalance, TransactionType.INCOME);
                }

        transactionRepository.delete(transaction);
    }

        private void validateExpenseBalance(
                        String email,
                        TransactionType type,
                        BigDecimal amount) {

                validateExpenseBalance(getBalance(email), type, amount);
        }

        private void validateExpenseBalance(
                        BigDecimal balance,
                        TransactionType type,
                        BigDecimal amount) {

                if (type == TransactionType.EXPENSE
                                && (balance.compareTo(BigDecimal.ZERO) <= 0
                                || amount.compareTo(balance) > 0)) {
                        throw new InsufficientBalanceException(
                                        "Expense cannot exceed the available balance"
                        );
                }
        }

        private void validateResultingBalance(
                        BigDecimal resultingBalance,
                        TransactionType type) {

                if (resultingBalance.compareTo(BigDecimal.ZERO) < 0) {
                        throw new InsufficientBalanceException(
                                        type == TransactionType.EXPENSE
                                                        ? "Expense cannot exceed the available balance"
                                                        : "This change would make the balance negative"
                        );
                }
        }

        private BigDecimal getBalance(String email) {

                BigDecimal income = transactionRepository.sumByUserEmailAndType(
                                email,
                                TransactionType.INCOME
                );
                BigDecimal expenses = transactionRepository.sumByUserEmailAndType(
                                email,
                                TransactionType.EXPENSE
                );

                return income.subtract(expenses);
        }

}