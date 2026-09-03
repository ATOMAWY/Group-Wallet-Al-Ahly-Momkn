package com.alahlymomkn.wallet.mapper;

import com.alahlymomkn.wallet.dto.WalletResponseDto;
import com.alahlymomkn.wallet.entity.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletResponseDto toResponseDto(Wallet wallet);
}
