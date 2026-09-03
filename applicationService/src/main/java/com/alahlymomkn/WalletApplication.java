package com.alahlymomkn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletApplication.class, args);
        System.out.println("✅ Al Ahly Momkn Group Wallet System Started Successfully!");
    }
}