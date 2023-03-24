package com.demo.forexbackend.service;

import com.demo.forexbackend.dto.BankAccountDto;
import com.demo.forexbackend.dto.RequestDto;
import com.demo.forexbackend.dto.TraderDto;
import com.demo.forexbackend.dto.WalletDto;
import com.demo.forexbackend.payload.BankAccountPayload;
import com.demo.forexbackend.payload.WalletPayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TraderService {
    TraderDto auth(Authentication authentication);
    Page<RequestDto> getRequests(Long traderId, Pageable pageable);
    RequestDto getRequest(Long traderId, Long requestId);
    WalletDto getWallet(Long traderId);
    WalletDto deposit(Long traderId, WalletPayload payload);
    List<BankAccountDto> getBankAccounts(Long traderId);
    BankAccountDto addBankAccount(Long traderId, BankAccountPayload payload);
}
