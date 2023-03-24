package com.demo.forexbackend.payload;

import com.demo.forexbackend.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestPayload {
    private Double amount;
    private Long traderId;
    private Long walletId;
    private Long exchangeId;
    private String sourceCurrency;
    private String targetCurrency;
    private Long bankAccountId;
}
