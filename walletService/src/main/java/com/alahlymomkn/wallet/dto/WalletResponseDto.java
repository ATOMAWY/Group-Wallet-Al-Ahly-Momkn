package com.alahlymomkn.wallet.dto;

import com.alahlymomkn.common.enums.WalletType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponseDto {
    private Long id;
    private BigDecimal balance;
    private WalletType type;
    private Long userId;
    private Long groupId;
    private Integer version;
}
