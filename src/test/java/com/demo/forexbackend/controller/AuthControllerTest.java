package com.demo.forexbackend.controller;

import com.demo.forexbackend.dto.TraderDto;
import com.demo.forexbackend.service.TraderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TraderService traderService;
    private TraderDto traderDto;

    @BeforeEach
    void setUp() {
        traderDto = TraderDto.builder().id(1L).name("Trader").email("trader@forex.dev").build();
    }

    @Test
    void traderAuth_GivenValidToken_ShouldReturnSuccess() throws Exception {
        when(traderService.auth(any())).thenReturn(traderDto);

        mockMvc.perform(get("/traders/auth").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    void traderAuth_GivenTraderExists_ShouldReturnTrader() throws Exception {
        when(traderService.auth(ArgumentMatchers.any())).thenReturn(traderDto);

        mockMvc.perform(get("/traders/auth")
                        .with(jwt())
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(1)));
    }

    @Test
    void signup_GivenTokenDoesNotContainEmailClaim_ShouldThrowException() throws Exception {
        when(traderService.auth(ArgumentMatchers.any())).thenThrow(new InvalidBearerTokenException("Invalid bearer token"));

        mockMvc.perform(get("/traders/auth")
                        .with(jwt())
                ).andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", is("Invalid bearer token")));
    }
}