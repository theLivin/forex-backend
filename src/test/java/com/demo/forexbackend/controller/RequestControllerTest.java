package com.demo.forexbackend.controller;

import com.demo.forexbackend.dto.RequestDto;
import com.demo.forexbackend.error.NotFoundException;
import com.demo.forexbackend.service.RequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = RequestController.class)
class RequestControllerTest {
    private final String REQUEST = """
            {
                "amount": 10.19,
                "traderId": 1,
                "exchangeId": 1,
                "sourceCurrency": "GHS",
                "targetCurrency": "NGN",
                "walletId": 1,
                "bankAccountId": 1
            }
            """;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RequestService requestService;

    @Test
    public void createRequest_GivenAnyRequiredEntityDoesNotExist_ShouldReturnNotFound() throws Exception {
        doThrow(NotFoundException.class).when(requestService).createRequest(any());

        mockMvc.perform(post("/requests").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createRequest_GivenValidData_ShouldReturnCreated() throws Exception {
        given(requestService.createRequest(any())).willReturn(new RequestDto());

        mockMvc.perform(post("/requests").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST))
                .andExpect(status().isCreated());
    }
}