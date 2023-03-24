package com.demo.forexbackend.dto;

import com.demo.forexbackend.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Status status;
    private Double rate;
    private Double amount;
    private TraderDto trader;
    private ExchangeDto exchange;
    private CurrencyDto sourceCurrency;
    private CurrencyDto targetCurrency;
    private String message;
    private WalletDto wallet;
    private BankAccountDto bankAccount;
}