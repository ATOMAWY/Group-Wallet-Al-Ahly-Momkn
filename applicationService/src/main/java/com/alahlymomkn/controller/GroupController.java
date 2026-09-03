package com.alahlymomkn.controller;

import com.alahlymomkn.common.enums.GroupRole;
import com.alahlymomkn.group.dto.GroupResponseDto;
import com.alahlymomkn.group.service.GroupService;
import com.alahlymomkn.transaction.dto.TransactionResponseDto;
import com.alahlymomkn.wallet.dto.WalletResponseDto;
import com.alahlymomkn.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<GroupResponseDto> createGroup(@RequestParam String name, @RequestHeader Long userId) {
        return ResponseEntity.ok(groupService.createGroup(name, userId));
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<String> addMember(@PathVariable Long groupId,
                                            @RequestParam Long newUserId,
                                            @RequestHeader Long userId) {
        groupService.addMember(userId, groupId, newUserId);
        return ResponseEntity.ok("Member added successfully");
    }

    @PutMapping("/{groupId}/members/{targetUserId}/role")
    public ResponseEntity<String> assignRole(@PathVariable Long groupId,
                                             @PathVariable Long targetUserId,
                                             @RequestParam GroupRole role,
                                             @RequestHeader Long userId) {
        groupService.assignRole(userId, groupId, targetUserId, role);
        return ResponseEntity.ok("Role updated successfully to: " + role);
    }

    @PostMapping("/{groupId}/deposit")
    public ResponseEntity<String> deposit(@PathVariable Long groupId,
                                          @RequestParam BigDecimal amount,
                                          @RequestHeader Long userId) {
        groupService.validateMemberAccess(userId, groupId);
        walletService.transferToGroup(userId, groupId, amount);
        return ResponseEntity.ok("Transfer of " + amount + " EGP successful!");
    }
    @GetMapping("/{groupId}/wallet")
    public ResponseEntity<WalletResponseDto> getGroupWalletBalance(@PathVariable Long groupId,
                                                                   @RequestHeader Long userId) {
        groupService.validateMemberAccess(userId, groupId);
        return ResponseEntity.ok(walletService.getGroupWallet(groupId));
    }

    @PostMapping("/{groupId}/expense")
    public ResponseEntity<String> executeExpense(@PathVariable Long groupId,
                                                 @RequestParam BigDecimal amount,
                                                 @RequestHeader Long userId) {
        walletService.executeExpense(userId, groupId, amount);
        return ResponseEntity.ok("Expense of " + amount + " EGP executed successfully!");
    }

    @GetMapping("/{groupId}/transactions")
    public ResponseEntity<List<TransactionResponseDto>> getGroupTransactions(@PathVariable Long groupId,
                                                                             @RequestHeader Long userId) {
        groupService.validateMemberAccess(userId, groupId);
        return ResponseEntity.ok(walletService.getGroupTransactions(groupId));
    }
}