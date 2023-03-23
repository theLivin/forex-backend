package com.demo.forexbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Wallet extends Account{
    @OneToOne(mappedBy = "wallet")
    private Trader trader;
}
