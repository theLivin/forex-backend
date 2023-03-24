package com.demo.forexbackend.controller;

import com.demo.forexbackend.dto.CurrencyDto;
import com.demo.forexbackend.dto.ExchangeDto;
import com.demo.forexbackend.dto.ResponseDto;
import com.demo.forexbackend.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/currencies")
    public ResponseEntity<ResponseDto<List<CurrencyDto>>> getCurrencies() {
        var data = currencyService.getCurrencies();
        var res = new ResponseDto<>(true, data);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
