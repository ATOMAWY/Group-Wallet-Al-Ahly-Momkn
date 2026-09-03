package com.alahlymomkn.wallet.service;

import com.alahlymomkn.common.enums.TransactionType;
import com.alahlymomkn.common.enums.WalletType;
import com.alahlymomkn.transaction.entity.Transaction;
import com.alahlymomkn.transaction.repository.TransactionRepository;
import com.alahlymomkn.wallet.entity.Wallet;
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
        Wallet userWallet = walletRepository.findByUserIdAndType(userId, WalletType.PERSONAL);

        Wallet groupWallet = walletRepository.findByGroupIdAndType(groupId, WalletType.GROUP);

        if (userWallet.getBalance().compareTo(amount) < 0) {
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
    }
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