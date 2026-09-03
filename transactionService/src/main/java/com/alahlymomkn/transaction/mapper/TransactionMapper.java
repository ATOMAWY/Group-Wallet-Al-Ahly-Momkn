package com.alahlymomkn.transaction.mapper;

import com.alahlymomkn.transaction.dto.TransactionResponseDto;
import com.alahlymomkn.transaction.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionResponseDto toResponseDto(Transaction transaction);
}
