package com.alahlymomkn.wallet.entity;

import com.alahlymomkn.common.enums.TransactionType;
import com.alahlymomkn.common.enums.WalletType;

import com.alahlymomkn.transaction.entity.Transaction;
import com.alahlymomkn.transaction.repository.TransactionRepository;
import com.alahlymomkn.wallet.repo.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void transferToGroup(Long userId, Long groupId, BigDecimal amount) {
        Wallet userWallet = walletRepository.findByUserIdAndType(userId, WalletType.PERSONAL)
                .orElseThrow(() -> new RuntimeException("User personal wallet not found"));

        Wallet groupWallet = walletRepository.findByGroupIdAndType(groupId, WalletType.GROUP)
                .orElseThrow(() -> new RuntimeException("Group wallet not found"));

        if (userWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds in your personal wallet!");
        }

        userWallet.setBalance(userWallet.getBalance().subtract(amount));
        groupWallet.setBalance(groupWallet.getBalance().add(amount));

        walletRepository.save(userWallet);
        walletRepository.save(groupWallet);

        Transaction record = Transaction.builder()
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .sourceWalletId(userWallet.getId())
                .destWalletId(groupWallet.getId())
                .performedByUserId(userId)
                .build();

        transactionRepository.save(record);
        System.out.println("✅ " + amount + " EGP transferred to Group " + groupId);
    }
    // Inside WalletService.java
    public void createGroupWallet(Long groupId) {
        Wallet groupWallet = Wallet.builder()
                .balance(BigDecimal.ZERO)
                .type(WalletType.GROUP)
                .groupId(groupId)
                .version(0)
                .build();
        walletRepository.save(groupWallet);
    }
}