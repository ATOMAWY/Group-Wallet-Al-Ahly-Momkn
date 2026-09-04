package com.alahlymomkn.transaction.service;

import com.alahlymomkn.common.enums.TransactionType;
import com.alahlymomkn.transaction.dto.TransactionResponseDto;
import com.alahlymomkn.transaction.entity.Transaction;
import com.alahlymomkn.transaction.mapper.TransactionMapper;
import com.alahlymomkn.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Transactional(propagation = Propagation.MANDATORY)
    public void record(BigDecimal amount,
                       TransactionType type,
                       Long sourceWalletId,
                       Long destWalletId,
                       Long performedByUserId) {
        Transaction tx = Transaction.builder()
                .amount(amount)
                .type(type)
                .sourceWalletId(sourceWalletId)
                .destWalletId(destWalletId)
                .performedByUserId(performedByUserId)
                .build();

        transactionRepository.save(tx);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getWalletStatement(Long walletId) {
        return transactionRepository
                .findBySourceWalletIdOrDestWalletIdOrderByCreatedAtDesc(walletId, walletId)
                .stream()
                .map(transactionMapper::toResponseDto)
                .toList();
    }
}
