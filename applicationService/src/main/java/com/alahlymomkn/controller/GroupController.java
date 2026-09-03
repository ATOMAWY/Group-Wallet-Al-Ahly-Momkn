package com.alahlymomkn.controller;

import com.alahlymomkn.group.entity.Group;
import com.alahlymomkn.group.service.GroupService;
import com.alahlymomkn.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final WalletService walletService;

    @PostMapping
    public Group createGroup(@RequestParam String name, @RequestHeader Long userId) {
        return groupService.createGroup(name, userId);
    }

    @PostMapping("/{groupId}/deposit")
    public String deposit(@PathVariable Long groupId, @RequestParam BigDecimal amount, @RequestHeader Long userId) {
        walletService.transferToGroup(userId, groupId, amount);
        return "Transfer of " + amount + " EGP successful!";
    }
}