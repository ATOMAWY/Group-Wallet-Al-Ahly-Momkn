package com.alahlymomkn.transaction.dto;

import com.alahlymomkn.common.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private Long sourceWalletId;
    private Long destWalletId;
    private Long performedByUserId;
    private LocalDateTime createdAt;
}
