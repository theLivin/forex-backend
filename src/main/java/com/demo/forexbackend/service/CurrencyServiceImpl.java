package com.demo.forexbackend.service;

import com.demo.forexbackend.config.EntityMapper;
import com.demo.forexbackend.dto.CurrencyDto;
import com.demo.forexbackend.dto.ExchangeDto;
import com.demo.forexbackend.entity.Currency;
import com.demo.forexbackend.error.NotFoundException;
import com.demo.forexbackend.repository.CurrencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService{
    private final CurrencyRepository currencyRepository;
    private final EntityMapper mapper;

    @Override
    public List<ExchangeDto> getExchanges(String currencyCode) {
        Currency currency = currencyRepository.findById(currencyCode).orElseThrow(() -> new NotFoundException(String.format("Currency with parameters {id=%s} is currently not available", currencyCode)));
        return currency.getExchanges().stream().map(mapper::exchangeToDto).toList();
    }

    @Override
    public List<CurrencyDto> getCurrencies() {
        return currencyRepository.findAll().stream().map(mapper::currencyToDto).toList();
    }
}
