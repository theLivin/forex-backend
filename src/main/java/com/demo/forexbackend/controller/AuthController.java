package com.demo.forexbackend.controller;

import com.demo.forexbackend.dto.ResponseDto;
import com.demo.forexbackend.dto.TraderDto;
import com.demo.forexbackend.service.TraderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final TraderService traderService;

    @GetMapping("/traders/auth")
    public ResponseEntity<ResponseDto<TraderDto>> auth(Authentication authentication) {
        var data = traderService.auth(authentication);
        var res = new ResponseDto<>(true, data);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
