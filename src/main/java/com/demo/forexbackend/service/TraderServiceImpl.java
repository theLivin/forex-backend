package com.demo.forexbackend.service;

import com.demo.forexbackend.config.EntityMapper;
import com.demo.forexbackend.dto.BankAccountDto;
import com.demo.forexbackend.dto.RequestDto;
import com.demo.forexbackend.dto.TraderDto;
import com.demo.forexbackend.dto.WalletDto;
import com.demo.forexbackend.entity.*;
import com.demo.forexbackend.error.NotFoundException;
import com.demo.forexbackend.error.NotNullException;
import com.demo.forexbackend.payload.BankAccountPayload;
import com.demo.forexbackend.payload.WalletPayload;
import com.demo.forexbackend.repository.BankAccountRepository;
import com.demo.forexbackend.repository.CurrencyRepository;
import com.demo.forexbackend.repository.TraderRepository;
import com.demo.forexbackend.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TraderServiceImpl implements TraderService {
    private final TraderRepository traderRepository;
    private final EntityMapper mapper;
    private final CurrencyRepository currencyRepository;
    private final WalletRepository walletRepository;
    private final BankAccountRepository bankAccountRepository;

    @Override
    public TraderDto auth(Authentication authentication) {
        var auth = (JwtAuthenticationToken) authentication;
        Map<String, Object> claims = auth.getTokenAttributes();
        String email = (String) claims.get("email");

        if (email == null) throw new InvalidBearerTokenException("Invalid bearer token");

        var existingTrader = traderRepository.findByEmail(email);
        if (existingTrader.isPresent()) return mapper.traderToDto(existingTrader.get());

        String name = (String) claims.get("name");

        var existingCurrency = currencyRepository.findById("GHS");
        Currency currency = existingCurrency.orElseGet(() -> currencyRepository.save(Currency.builder().code("GHS").country("Ghana").name("Ghana Cedi").build()));

        Wallet wallet = new Wallet();
        wallet.setCurrency(currency);
        var defaultWallet = walletRepository.save(wallet);

        Trader trader = Trader.builder().name(name).email(email).wallet(defaultWallet).build();

        return mapper.traderToDto(traderRepository.save(trader));
    }

    @Override
    public Page<RequestDto> getRequests(Long traderId, Pageable pageable) {
        Trader trader = traderRepository.findById(traderId).orElseThrow(() -> new NotFoundException(String.format("Trader was not found with parameters {id=%d}", traderId)));

        var requests = trader.getRequests();
        var res = requests.stream().map(mapper::requestToDto).toList();
        return new PageImpl<>(res, pageable, requests.size());
    }

    @Override
    public RequestDto getRequest(Long traderId, Long requestId) {
        Trader trader = traderRepository.findById(traderId).orElseThrow(() -> new NotFoundException(String.format("Trader was not found with parameters {id=%d}", traderId)));

        var requests = trader.getRequests();

        for (Request request : requests) {
            if (Objects.equals(request.getId(), requestId)) return mapper.requestToDto(request);
        }

        throw new NotFoundException(String.format("Request was not found with parameters {id=%d}", requestId));
    }

    @Override
    public WalletDto getWallet(Long traderId) {
        Trader trader = traderRepository.findById(traderId).orElseThrow(() -> new NotFoundException(String.format("Trader was not found with parameters {id=%d}", traderId)));
        return mapper.walletToDto(trader.getWallet());
    }

    @Override
    public WalletDto deposit(Long traderId, WalletPayload payload) {
        Trader trader = traderRepository.findById(traderId).orElseThrow(() -> new NotFoundException(String.format("Trader was not found with parameters {id=%d}", traderId)));
        var wallet = trader.getWallet();
        wallet.setBalance(wallet.getBalance() + payload.getAmount());
        var res = walletRepository.save(wallet);
        return mapper.walletToDto(res);
    }

    @Override
    public List<BankAccountDto> getBankAccounts(Long traderId) {
        Trader trader = traderRepository.findById(traderId).orElseThrow(() -> new NotFoundException(String.format("Trader was not found with parameters {id=%d}", traderId)));
        return trader.getBankAccounts().stream().map(mapper::bankAccountToDto).toList();
    }

    @Override
    public BankAccountDto addBankAccount(Long traderId, BankAccountPayload payload) {
        Trader trader = traderRepository.findById(traderId).orElseThrow(() -> new NotFoundException(String.format("Trader was not found with parameters {id=%d}", traderId)));
        Currency currency = currencyRepository.findById(payload.getCurrency()).orElseThrow(() -> new NotFoundException(String.format("Currency was not found with parameters {id=%s}", payload.getCurrency())));
        if(Objects.equals(payload.getName(), "")) throw new NotNullException("Name field cannot be null");
        BankAccount bankAccount = BankAccount.builder().name(payload.getName()).build();
        bankAccount.setCurrency(currency);
        bankAccount.setTrader(trader);

        var res = bankAccountRepository.save(bankAccount);
        return mapper.bankAccountToDto(res);
    }
}
