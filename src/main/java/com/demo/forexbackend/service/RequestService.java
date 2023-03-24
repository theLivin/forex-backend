package com.demo.forexbackend.service;

import com.demo.forexbackend.dto.RequestDto;
import com.demo.forexbackend.payload.RequestPayload;


public interface RequestService {
    RequestDto createRequest(RequestPayload payload);
}
