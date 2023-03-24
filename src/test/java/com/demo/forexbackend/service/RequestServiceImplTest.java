package com.demo.forexbackend.service;

import com.demo.forexbackend.config.EntityMapper;
import com.demo.forexbackend.dto.*;
import com.demo.forexbackend.entity.*;
import com.demo.forexbackend.error.NotFoundException;
import com.demo.forexbackend.payload.RequestPayload;
import com.demo.forexbackend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    @Mock
    RequestRepository requestRepository;
    @Mock
    ExchangeRepository exchangeRepository;
    @Mock
    CurrencyRepository currencyRepository;
    @Mock
    WalletRepository walletRepository;
    @Mock
    BankAccountRepository bankAccountRepository;
    @Mock
    TraderRepository traderRepository;
    @Mock
    private EntityMapper mapper;
    @InjectMocks
    RequestServiceImpl requestService;

    private Trader trader;
    private Provider provider;
    private Exchange exchange;
    private Currency sourceCurrency;
    private Currency targetCurrency;
    private Wallet wallet;
    private BankAccount bankAccount;

    private RequestPayload payload = RequestPayload.builder().amount(10.0)
            .traderId(1L).exchangeId(1L).sourceCurrency("GHS").targetCurrency("NGN")
            .walletId(1L).bankAccountId(1L).build();

    @BeforeEach
    void setUp() {
        trader = Trader.builder().email("trader@forex.dev").name("Trader").build();
        provider = Provider.builder().name("Provider").build();
        exchange = Exchange.builder().rate(0.5).provider(provider).build();
        sourceCurrency = Currency.builder().code("GHS").build();
        targetCurrency = Currency.builder().code("NGN").build();
        wallet = Wallet.builder().trader(trader).build();
        wallet.setBalance(100.0);
        wallet.setCurrency(sourceCurrency);
        bankAccount = BankAccount.builder().trader(trader).build();
        bankAccount.setCurrency(targetCurrency);
    }

    @Test
    void createRequest_GivenTraderDoesNotExist_ShouldReturnNotFound() {
        when(traderRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> requestService.createRequest(payload));
    }

    @Test
    void createRequest_GivenWalletDoesNotExist_ShouldReturnNotFound() {
        when(traderRepository.findById(any())).thenReturn(Optional.of(trader));
        when(walletRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> requestService.createRequest(payload));
    }

    @Test
    void createRequest_GivenExchangeDoesNotExist_ShouldReturnNotFound() {
        when(traderRepository.findById(any())).thenReturn(Optional.of(trader));
        when(walletRepository.findById(any())).thenReturn(Optional.of(wallet));
        when(exchangeRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> requestService.createRequest(payload));
    }

    @Test
    void createRequest_GivenSourceCurrencyDoesNotExist_ShouldReturnNotFound() {
        when(traderRepository.findById(any())).thenReturn(Optional.of(trader));
        when(walletRepository.findById(any())).thenReturn(Optional.of(wallet));
        when(exchangeRepository.findById(any())).thenReturn(Optional.of(exchange));
        when(currencyRepository.findById(sourceCurrency.getCode())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> requestService.createRequest(payload));
    }

    @Test
    void createRequest_GivenTargetCurrencyDoesNotExist_ShouldReturnNotFound() {
        when(traderRepository.findById(any())).thenReturn(Optional.of(trader));
        when(walletRepository.findById(any())).thenReturn(Optional.of(wallet));
        when(exchangeRepository.findById(any())).thenReturn(Optional.of(exchange));
        when(currencyRepository.findById(sourceCurrency.getCode())).thenReturn(Optional.of(sourceCurrency));
        when(currencyRepository.findById(targetCurrency.getCode())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> requestService.createRequest(payload));
    }

    @Test
    void createRequest_GivenBankAccountDoesNotExist_ShouldReturnNotFound() {
        when(traderRepository.findById(any())).thenReturn(Optional.of(trader));
        when(walletRepository.findById(any())).thenReturn(Optional.of(wallet));
        when(exchangeRepository.findById(any())).thenReturn(Optional.of(exchange));
        when(currencyRepository.findById(sourceCurrency.getCode())).thenReturn(Optional.of(sourceCurrency));
        when(currencyRepository.findById(targetCurrency.getCode())).thenReturn(Optional.of(targetCurrency));
        when(bankAccountRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> requestService.createRequest(payload));
    }

    @Test
    void createRequest_GivenTraderHasSufficientFunds_ShouldCreateCompleteRequest() {
        when(traderRepository.findById(any())).thenReturn(Optional.of(trader));
        when(walletRepository.findById(any())).thenReturn(Optional.of(wallet));
        when(exchangeRepository.findById(any())).thenReturn(Optional.of(exchange));
        when(currencyRepository.findById(sourceCurrency.getCode())).thenReturn(Optional.of(sourceCurrency));
        when(currencyRepository.findById(targetCurrency.getCode())).thenReturn(Optional.of(targetCurrency));
        when(bankAccountRepository.findById(any())).thenReturn(Optional.of(bankAccount));
        when(mapper.requestToDto(any())).thenReturn(RequestDto.builder().status(Status.COMPLETED).build());
        var res = requestService.createRequest(payload);


        assertEquals(Status.COMPLETED, res.getStatus());
    }

    @Test
    void createRequest_GivenTraderHasInsufficientFunds_ShouldCreateFailRequest() {
        when(traderRepository.findById(any())).thenReturn(Optional.of(trader));
        Wallet poorWallet = new Wallet();
        poorWallet.setCurrency(sourceCurrency);
        when(walletRepository.findById(any())).thenReturn(Optional.of(poorWallet));
        when(exchangeRepository.findById(any())).thenReturn(Optional.of(exchange));
        when(currencyRepository.findById(sourceCurrency.getCode())).thenReturn(Optional.of(sourceCurrency));
        when(currencyRepository.findById(targetCurrency.getCode())).thenReturn(Optional.of(targetCurrency));
        when(bankAccountRepository.findById(any())).thenReturn(Optional.of(bankAccount));
        when(mapper.requestToDto(any())).thenReturn(RequestDto.builder().status(Status.FAILED).build());
        var res = requestService.createRequest(payload);


        assertEquals(Status.FAILED, res.getStatus());
    }
}