package com.demo.forexbackend.service;

import com.demo.forexbackend.dto.ExchangeDto;

import java.util.List;

public interface CurrencyService {
    List<ExchangeDto> getExchanges(String currencyCode);
}
