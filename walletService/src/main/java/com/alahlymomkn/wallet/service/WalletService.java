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
import com.alahlymomkn.transaction.service.TransactionService;
import com.alahlymomkn.wallet.dto.WalletResponseDto;
import com.alahlymomkn.wallet.entity.Wallet;
import com.alahlymomkn.wallet.mapper.WalletMapper;
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
    private final GroupMemberRepository memberRepository;
    private final WalletMapper walletMapper;
    private final TransactionService transactionService;


    @Transactional(readOnly = true)
    public WalletResponseDto getGroupWallet(Long groupId) {
        return walletMapper.toResponseDto(findGroupWallet(groupId));
    }

    @Transactional(readOnly = true)
    public WalletResponseDto getPersonalWallet(Long userId) {
        return walletMapper.toResponseDto(findPersonalWallet(userId));
    }

    @Transactional(readOnly = true)
    public BigDecimal getPersonalBalance(Long userId) {
        return findPersonalWallet(userId).getBalance();
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getGroupTransactions(Long groupId) {
        Wallet groupWallet = findGroupWallet(groupId);
        return transactionService.getWalletStatement(groupWallet.getId());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getPersonalTransactions(Long userId) {
        Wallet personalWallet = findPersonalWallet(userId);
        return transactionService.getWalletStatement(personalWallet.getId());
    }



    @Transactional
    public void createGroupWallet(Long groupId) {
        Wallet groupWallet = Wallet.builder()
                .balance(BigDecimal.ZERO)
                .type(WalletType.GROUP)
                .groupId(groupId)
                .version(0)
                .build();
        walletRepository.save(groupWallet);
    }

    @Transactional
    public void topUpPersonalWallet(Long userId, BigDecimal amount) {
        validatePositiveAmount(amount);

        Wallet wallet = walletRepository.findByUserIdAndType(userId, WalletType.PERSONAL)
                .orElseGet(() -> createInitialPersonalWallet(userId));

        credit(wallet, amount);

        transactionService.record(amount, TransactionType.TOP_UP, null, wallet.getId(), userId);
    }

    @Transactional
    public void transferToGroup(Long userId, Long groupId, BigDecimal amount) {
        validatePositiveAmount(amount);

        Wallet userWallet = findPersonalWallet(userId);
        Wallet groupWallet = findGroupWallet(groupId);

        debit(userWallet, amount, "Insufficient funds in personal wallet");
        credit(groupWallet, amount);

        transactionService.record(amount, TransactionType.DEPOSIT, userWallet.getId(), groupWallet.getId(), userId);
    }

    @Transactional
    public void executeExpense(Long treasurerUserId, Long groupId, BigDecimal amount) {
        validatePositiveAmount(amount);
        verifyTreasurerRole(groupId, treasurerUserId);

        Wallet groupWallet = findGroupWallet(groupId);
        debit(groupWallet, amount, "Insufficient funds in group wallet");

        transactionService.record(amount, TransactionType.EXPENSE, groupWallet.getId(), null, treasurerUserId);
    }

    // --- Private Domain Helpers ---

    private void debit(Wallet wallet, BigDecimal amount, String failureMessage) {
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(failureMessage);
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
    }

    private void credit(Wallet wallet, BigDecimal amount) {
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }

    private Wallet findPersonalWallet(Long userId) {
        return walletRepository.findByUserIdAndType(userId, WalletType.PERSONAL)
                .orElseThrow(() -> new ResourceNotFoundException("Personal wallet not found for user: " + userId));
    }

    private Wallet findGroupWallet(Long groupId) {
        return walletRepository.findByGroupIdAndType(groupId, WalletType.GROUP)
                .orElseThrow(() -> new ResourceNotFoundException("Group wallet not found for group: " + groupId));
    }

    private Wallet createInitialPersonalWallet(Long userId) {
        return walletRepository.save(
                Wallet.builder()
                        .userId(userId)
                        .balance(BigDecimal.ZERO)
                        .type(WalletType.PERSONAL)
                        .version(0)
                        .build()
        );
    }

    private void verifyTreasurerRole(Long groupId, Long userId) {
        GroupMember member = memberRepository.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(() -> new AccessDeniedException("User is not a member of this group"));

        if (!member.getRoles().contains(GroupRole.TREASURER)) {
            throw new AccessDeniedException("Only treasurers can execute expenses from the group wallet");
        }
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be strictly positive");
        }
    }
}