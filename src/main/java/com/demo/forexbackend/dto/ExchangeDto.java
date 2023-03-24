package com.demo.forexbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeDto {
    private Long id;
    private CurrencyDto currency;
    private ProviderDto provider;
    private Double rate;
}
