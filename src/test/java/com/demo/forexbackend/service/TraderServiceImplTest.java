package com.demo.forexbackend.service;

import com.demo.forexbackend.config.EntityMapper;
import com.demo.forexbackend.dto.RequestDto;
import com.demo.forexbackend.dto.TraderDto;
import com.demo.forexbackend.entity.Currency;
import com.demo.forexbackend.entity.Request;
import com.demo.forexbackend.entity.Trader;
import com.demo.forexbackend.entity.Wallet;
import com.demo.forexbackend.error.NotFoundException;
import com.demo.forexbackend.repository.CurrencyRepository;
import com.demo.forexbackend.repository.TraderRepository;
import com.demo.forexbackend.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TraderServiceImplTest {
    @Mock
    TraderRepository traderRepository;
    @Mock
    CurrencyRepository currencyRepository;
    @Mock
    WalletRepository walletRepository;
    @InjectMocks
    private TraderServiceImpl traderService;
    @Mock
    private EntityMapper mapper;

    private JwtAuthenticationToken token;
    private TraderDto traderDto;
    private Trader trader;
    private Currency currency;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        String value = "token";

        var email = "trader@forex.dev";
        var name = "Trader";
        Instant issuedAt = Instant.now();
        Instant expiredAt = Instant.now().plusSeconds(100000);
        var headers = Map.of("aud", (Object) "aud");
        var claims = Map.of("email", email, "name", (Object) name);
        Jwt jwt = new Jwt(value, issuedAt, expiredAt, headers, claims);

        token = new JwtAuthenticationToken(jwt);

        traderDto = TraderDto.builder().email(email).name(name).build();
        trader = Trader.builder().email(email).name(name).requests(List.of(Request.builder().id(1L).build(), Request.builder().id(2L).build())).build();

        currency = Currency.builder().code("GHS").country("Ghana").name("Ghana Cedi").build();
        wallet = new Wallet();
        wallet.setCurrency(currency);
        wallet.setBalance(0.0);
        wallet.setId(1L);
    }

    @Test
    void auth_GivenTraderDoesNotExist_ShouldCreateTrader() {
        when(traderRepository.save(any())).thenReturn(trader);
        when(currencyRepository.findById(any())).thenReturn(Optional.of(currency));
        when(walletRepository.save(any())).thenReturn(wallet);
        when(mapper.traderToDto(trader)).thenReturn(traderDto);
        assertEquals(traderDto, traderService.auth(token));
    }

    @Test
    void auth_GivenTraderExists_ShouldReturnTrader() {
        when(traderRepository.findByEmail(any())).thenReturn(Optional.of(trader));
        when(mapper.traderToDto(trader)).thenReturn(traderDto);
        assertEquals(traderDto, traderService.auth(token));
    }

    @Test
    public void getRequests_GivenTraderDoesNotExist_ShouldReturnNotFound() {
        when(traderRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> traderService.getRequests(1L, PageRequest.of(0, 5)));
    }


    @Test
    public void getRequests_GivenTraderExists_ShouldReturnRequests() throws Exception {
        when(traderRepository.findById(any())).thenReturn(Optional.of(trader));
        var requests = traderService.getRequests(1L, PageRequest.of(0, 5));
        assertEquals(2, requests.getTotalElements());
    }

    @Test
    void getRequest_GivenTraderDoesNotExist_ShouldNotFound() {
        when(traderRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> traderService.getRequest(1L, 1L));
    }

    @Test
    void getRequest_GivenRequestDoesNotExist_ShouldNotFound() {
        when(traderRepository.findById(any())).thenReturn(Optional.of(trader));
        assertThrows(NotFoundException.class, () -> traderService.getRequest(1L, 100L));
    }

    @Test
    void getRequest_GivenTraderHasRequestWithId_ShouldReturnRequest() {
        when(traderRepository.findById(any())).thenReturn(Optional.of(trader));
        when(mapper.requestToDto(any())).thenReturn(RequestDto.builder().id(1L).build());
        var res = traderService.getRequest(1L, 1L);
        assertEquals(1, res.getId());
    }
}