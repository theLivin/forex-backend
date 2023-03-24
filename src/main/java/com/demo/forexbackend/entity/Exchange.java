package com.demo.forexbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exchange extends Account{
    @ManyToOne
    @JoinColumn(name = "provider")
    @JsonIgnoreProperties("exchanges")
    private Provider provider;

    private Double rate;
}
