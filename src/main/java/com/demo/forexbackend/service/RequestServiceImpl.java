package com.demo.forexbackend.service;

import com.demo.forexbackend.config.EntityMapper;
import com.demo.forexbackend.dto.RequestDto;
import com.demo.forexbackend.entity.Request;
import com.demo.forexbackend.entity.Status;
import com.demo.forexbackend.error.ConflictException;
import com.demo.forexbackend.error.NotFoundException;
import com.demo.forexbackend.payload.RequestPayload;
import com.demo.forexbackend.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final ExchangeRepository exchangeRepository;
    private final CurrencyRepository currencyRepository;
    private final WalletRepository walletRepository;
    private final BankAccountRepository bankAccountRepository;
    private final TraderRepository traderRepository;
    private final EntityMapper mapper;

    @Override
    public RequestDto createRequest(RequestPayload payload) {
        var amount = payload.getAmount();
        var traderId = payload.getTraderId();
        var walletId = payload.getWalletId();
        var exchangeId = payload.getExchangeId();
        var sourceCurrencyId = payload.getSourceCurrency();
        var targetCurrencyId = payload.getTargetCurrency();
        var bankAccountId = payload.getBankAccountId();
        var errorMessageNum = "%s was not found with parameters {id=%d}";
        var errorMessageStr = "%s was not found with parameters {id=%s}";

        var trader = traderRepository.findById(traderId).orElseThrow(() -> new NotFoundException(String.format(errorMessageNum, "Trader", traderId)));
        var wallet = walletRepository.findById(walletId).orElseThrow(() -> new NotFoundException(String.format(errorMessageNum, "Wallet", walletId)));
        var exchange = exchangeRepository.findById(exchangeId).orElseThrow(() -> new NotFoundException(String.format(errorMessageNum, "Exchange", exchangeId)));
        var sourceCurrency = currencyRepository.findById(sourceCurrencyId).orElseThrow(() -> new NotFoundException(String.format(errorMessageStr, "Source Currency", sourceCurrencyId)));
        var targetCurrency = currencyRepository.findById(targetCurrencyId).orElseThrow(() -> new NotFoundException(String.format(errorMessageStr, "Target Currency", targetCurrencyId)));
        var bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new NotFoundException(String.format(errorMessageNum, "Bank Account", bankAccountId)));

        if (wallet.getCurrency() != sourceCurrency) throw new ConflictException(String.format("Cannot purchase %s from %s wallet", sourceCurrency.getCode(), wallet.getCurrency().getCode()));
        if (bankAccount.getCurrency() != targetCurrency) throw new ConflictException(String.format("Cannot transfer %s to %s bank account", targetCurrency.getCode(), bankAccount.getCurrency().getCode()));

        Request request = Request.builder()
                .amount(amount).rate(exchange.getRate()).trader(trader).exchange(exchange)
                .sourceCurrency(sourceCurrency).targetCurrency(targetCurrency).wallet(wallet).bankAccount(bankAccount)
                .build();

        if(amount > wallet.getBalance()) {
            request.setStatus(Status.FAILED);
            request.setMessage("Unsuccessful due to insufficient balance in wallet");
        }
        else {
            request.setStatus(Status.COMPLETED);
            request.setMessage("Successful");
            wallet.setBalance(wallet.getBalance() - amount);
            bankAccount.setBalance(bankAccount.getBalance() + (amount * exchange.getRate()));
            // TODO: Update provider's account balance
        }

        return mapper.requestToDto(requestRepository.save(request));
    }
}
