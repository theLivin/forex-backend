package com.demo.forexbackend.controller;


import com.demo.forexbackend.dto.RequestDto;
import com.demo.forexbackend.error.NotFoundException;
import com.demo.forexbackend.service.TraderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TraderController.class)
class TraderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TraderService traderService;

    @Test
    public void getRequests_GivenTraderDoesNotExist_ShouldReturnNotFound() throws Exception {
        doThrow(NotFoundException.class).when(traderService).getRequests(any(), any(Pageable.class));

        mockMvc.perform(get("/traders/1/requests").with(jwt())).andExpect(status().isNotFound());
    }


    @Test
    public void getRequests_GivenTraderExists_ShouldReturnRequests() throws Exception {
        var requests = new PageImpl<RequestDto>(new ArrayList<>());

        when(traderService.getRequests(any(), any(Pageable.class)))
                .thenReturn(requests);

        mockMvc.perform(get("/traders/1/requests").with(jwt())).andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.totalElements", is(0)));
    }

    @Test
    public void getRequest_GivenEitherTraderOrRequestDoesNotExist_ShouldReturnNotFound() throws Exception {
        doThrow(NotFoundException.class).when(traderService).getRequest(any(), any());

        mockMvc.perform(get("/traders/1/requests/1").with(jwt())).andExpect(status().isNotFound());
    }


    @Test
    public void getRequest_GivenBothTraderAndRequestExist_ShouldReturnRequest() throws Exception {
        var request = new RequestDto();

        when(traderService.getRequest(any(), any()))
                .thenReturn(request);

        mockMvc.perform(get("/traders/1/requests/1").with(jwt())).andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }
}