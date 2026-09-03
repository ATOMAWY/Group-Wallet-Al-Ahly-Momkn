package com.alahlymomkn.wallet.service;

import com.alahlymomkn.common.enums.GroupRole;
import com.alahlymomkn.common.enums.TransactionType;
import com.alahlymomkn.common.enums.WalletType;
import com.alahlymomkn.common.exceptions.AccessDeniedException;
import com.alahlymomkn.common.exceptions.InsufficientFundsException;
import com.alahlymomkn.common.exceptions.ResourceNotFoundException;
import com.alahlymomkn.group.entity.GroupMember;
import com.alahlymomkn.group.repo.GroupMemberRepository;
import com.alahlymomkn.transaction.dto.TransactionResponseDto;
import com.alahlymomkn.transaction.entity.Transaction;
import com.alahlymomkn.transaction.mapper.TransactionMapper;
import com.alahlymomkn.transaction.repository.TransactionRepository;
import com.alahlymomkn.wallet.entity.Wallet;
import com.alahlymomkn.wallet.repo.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final GroupMemberRepository memberRepository;

    @Transactional
    public void transferToGroup(Long userId, Long groupId, BigDecimal amount) {
        Wallet userWallet = walletRepository.findByUserIdAndType(userId, WalletType.PERSONAL)
                .orElseThrow(() -> new ResourceNotFoundException("Personal wallet not found for user: " + userId));

        Wallet groupWallet = walletRepository.findByGroupIdAndType(groupId, WalletType.GROUP)
                .orElseThrow(() -> new ResourceNotFoundException("Group wallet not found for group: " + groupId));

        if (userWallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in personal wallet");
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

    @Transactional
    public void executeExpense(Long treasurerUserId, Long groupId, BigDecimal amount) {
        GroupMember member = memberRepository.findByGroupIdAndUserId(groupId, treasurerUserId)
                .orElseThrow(() -> new AccessDeniedException("User is not a member of this group"));

        if (!member.getRoles().contains(GroupRole.TREASURER)) {
            throw new AccessDeniedException("Only treasurers can execute expenses from the group wallet");
        }

        Wallet groupWallet = walletRepository.findByGroupIdAndType(groupId, WalletType.GROUP)
                .orElseThrow(() -> new ResourceNotFoundException("Group wallet not found for group: " + groupId));

        if (groupWallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in group wallet");
        }

        groupWallet.setBalance(groupWallet.getBalance().subtract(amount));
        walletRepository.save(groupWallet);

        Transaction record = Transaction.builder()
                .amount(amount)
                .type(TransactionType.EXPENSE)
                .sourceWalletId(groupWallet.getId())
                .destWalletId(null)
                .performedByUserId(treasurerUserId)
                .build();

        transactionRepository.save(record);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getGroupTransactions(Long groupId) {
        Wallet groupWallet = walletRepository.findByGroupIdAndType(groupId, WalletType.GROUP)
                .orElseThrow(() -> new ResourceNotFoundException("Group wallet not found for group: " + groupId));

        return transactionRepository.findBySourceWalletIdOrDestWalletIdOrderByCreatedAtDesc(
                        groupWallet.getId(), groupWallet.getId()
                ).stream()
                .map(transactionMapper::toResponseDto)
                .toList();
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

    @Transactional(readOnly = true)
    public BigDecimal getPersonalBalance(Long userId) {
        Wallet wallet = walletRepository.findByUserIdAndType(userId, WalletType.PERSONAL)
                .orElseThrow(() -> new ResourceNotFoundException("Personal wallet not found for user: " + userId));
        return wallet.getBalance();
    }

    @Transactional
    public void topUpPersonalWallet(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserIdAndType(userId, WalletType.PERSONAL)
                .orElseThrow(() -> new ResourceNotFoundException("Personal wallet not found for user: " + userId));

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }

}