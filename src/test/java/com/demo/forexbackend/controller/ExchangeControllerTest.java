package com.demo.forexbackend.controller;

import com.demo.forexbackend.dto.ExchangeDto;
import com.demo.forexbackend.error.NotFoundException;
import com.demo.forexbackend.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ExchangeController.class)
class ExchangeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CurrencyService currencyService;

    @Test
    public void getExchanges_GivenCurrencyDoesNotExist_ShouldReturnNotFound() throws Exception {
        doThrow(NotFoundException.class).when(currencyService).getExchanges(any());

        mockMvc.perform(get("/exchanges/NGN").with(jwt())).andExpect(status().isNotFound());
    }


    @Test
    public void getExchanges_GivenCurrencyExists_ShouldReturnExchanges() throws Exception {
        var exchanges = List.of(new ExchangeDto());

        when(currencyService.getExchanges(any()))
                .thenReturn(exchanges);

        mockMvc.perform(get("/exchanges/NGN").with(jwt())).andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }
}