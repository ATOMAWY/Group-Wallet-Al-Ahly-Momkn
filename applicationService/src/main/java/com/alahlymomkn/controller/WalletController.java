package com.alahlymomkn.controller;

import com.alahlymomkn.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<BigDecimal> getPersonalBalance(@RequestHeader Long userId) {
        return ResponseEntity.ok(walletService.getPersonalBalance(userId));
    }

    @PostMapping
    public ResponseEntity<BigDecimal> topUpPersonalWallet(@RequestHeader Long userId,
                                                        @RequestParam BigDecimal amount) {
        walletService.topUpPersonalWallet(userId, amount);
        return ResponseEntity.ok(walletService.getPersonalBalance(userId));
    }
}
