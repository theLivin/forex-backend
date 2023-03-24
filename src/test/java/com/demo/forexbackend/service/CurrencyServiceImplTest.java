package com.demo.forexbackend.service;

import com.demo.forexbackend.config.EntityMapper;
import com.demo.forexbackend.dto.ExchangeDto;
import com.demo.forexbackend.entity.Currency;
import com.demo.forexbackend.entity.Exchange;
import com.demo.forexbackend.error.NotFoundException;
import com.demo.forexbackend.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplTest {
    @Mock
    CurrencyRepository currencyRepository;
    @InjectMocks
    private CurrencyServiceImpl currencyService;
    @Mock
    private EntityMapper mapper;
    private Currency currency;

    @BeforeEach
    void setUp() {
        currency = Currency.builder().code("GHS").country("Ghana").name("Ghana Cedi").build();
        currency.setExchanges(List.of(new Exchange(), new Exchange()));
    }

    @Test
    public void getExchanges_GivenCurrencyDoesNotExist_ShouldReturnNotFound() {
        when(currencyRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> currencyService.getExchanges("GHS"));
    }


    @Test
    public void getRequests_GivenTraderExists_ShouldReturnRequests() {
        when(currencyRepository.findById(any())).thenReturn(Optional.of(currency));
        when(mapper.exchangeToDto(any())).thenReturn(new ExchangeDto());
        var requests = currencyService.getExchanges("GHS");
        assertEquals(2, requests.size());
    }
}