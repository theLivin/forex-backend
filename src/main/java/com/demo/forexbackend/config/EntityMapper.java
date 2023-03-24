package com.demo.forexbackend.config;


import com.demo.forexbackend.dto.*;
import com.demo.forexbackend.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@Component
public interface EntityMapper {
    TraderDto traderToDto(Trader trader);
    RequestDto requestToDto(Request request);
    ExchangeDto exchangeToDto(Exchange exchange);
    WalletDto walletToDto(Wallet wallet);
    BankAccountDto bankAccountToDto(BankAccount bankAccount);
    CurrencyDto currencyToDto(Currency currency);
}
