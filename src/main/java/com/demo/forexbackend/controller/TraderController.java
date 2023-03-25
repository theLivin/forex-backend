package com.demo.forexbackend.controller;

import com.demo.forexbackend.dto.BankAccountDto;
import com.demo.forexbackend.dto.RequestDto;
import com.demo.forexbackend.dto.ResponseDto;
import com.demo.forexbackend.dto.WalletDto;
import com.demo.forexbackend.payload.BankAccountPayload;
import com.demo.forexbackend.payload.WalletPayload;
import com.demo.forexbackend.service.RequestService;
import com.demo.forexbackend.service.TraderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TraderController {
    private final TraderService traderService;

    @GetMapping("/traders/{id}/requests")
    public ResponseEntity<ResponseDto<Page<RequestDto>>> getRequests(@PathVariable("id") Long traderId,
                                                                     @RequestParam(name = "page_number", defaultValue = "0") Integer pageNumber,
                                                                     @RequestParam(name = "page_size", defaultValue = "20") Integer pageSize) {
        var data = traderService.getRequests(traderId, PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending()));
        var res = new ResponseDto<>(true, data);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/traders/{traderId}/wallet")
    public ResponseEntity<ResponseDto<WalletDto>> getWallet(@PathVariable("traderId") Long traderId) {
        var data = traderService.getWallet(traderId);
        var res = new ResponseDto<>(true, data);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping("/traders/{traderId}/wallet")
    public ResponseEntity<ResponseDto<WalletDto>> deposit(@PathVariable("traderId") Long traderId, @RequestBody WalletPayload payload) {
        var data = traderService.deposit(traderId, payload);
        var res = new ResponseDto<>(true, data);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/traders/{traderId}/requests/{requestId}")
    public ResponseEntity<ResponseDto<RequestDto>> getRequest(@PathVariable("traderId") Long traderId,
                                                              @PathVariable("traderId") Long requestId) {
        var data = traderService.getRequest(traderId, requestId);
        var res = new ResponseDto<>(true, data);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping("/traders/{traderId}/bank-accounts")
    public ResponseEntity<ResponseDto<BankAccountDto>> addBankAccount(@PathVariable("traderId") Long traderId, @RequestBody BankAccountPayload payload) {
        var data = traderService.addBankAccount(traderId, payload);
        var res = new ResponseDto<>(true, data);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/traders/{traderId}/bank-accounts")
    public ResponseEntity<ResponseDto<List<BankAccountDto>>> getBankAccounts(@PathVariable("traderId") Long traderId) {
        var data = traderService.getBankAccounts(traderId);
        var res = new ResponseDto<>(true, data);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
