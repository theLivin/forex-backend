package com.demo.forexbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;


@MappedSuperclass
@JsonIgnoreProperties({ "exchanges" })
public class Account {
    @Id
    private Long id;
    private Double balance = 0.0;
    @ManyToOne
    @JoinColumn(name = "currency")
    private Currency currency;
}
