package com.demo.forexbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BankAccount extends Account{
    private String name;

    @ManyToOne
    @JoinColumn(name = "trader")
    @JsonIgnoreProperties("bankAccounts")
    private Trader trader;
}
