package com.demo.forexbackend.controller;

import com.demo.forexbackend.dto.RequestDto;
import com.demo.forexbackend.dto.ResponseDto;
import com.demo.forexbackend.payload.RequestPayload;
import com.demo.forexbackend.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/requests")
    public ResponseEntity<ResponseDto<RequestDto>> createRequest(@RequestBody RequestPayload payload) {
        var data = requestService.createRequest(payload);
        var res = new ResponseDto<>(true, data);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
