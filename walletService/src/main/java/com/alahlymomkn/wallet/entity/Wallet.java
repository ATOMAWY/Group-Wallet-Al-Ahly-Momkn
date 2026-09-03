package com.alahlymomkn.wallet.entity;

import com.alahlymomkn.common.enums.WalletType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private WalletType type;

    private Long userId;
    private Long groupId;

    @Version
    private Integer version;
}