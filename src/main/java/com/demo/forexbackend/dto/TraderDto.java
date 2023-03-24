package com.demo.forexbackend.dto;

import com.demo.forexbackend.entity.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraderDto {
    private Long id;
    private String name;
    private String email;
}
