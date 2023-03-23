package com.demo.forexbackend.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(length = 32, columnDefinition = "varchar(32) default 'PENDING'")
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @NotBlank(message = "rate field is required")
    private Double rate;

    @Column(nullable = false)
    @NotBlank(message = "amount field is required")
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "trader")
    @JsonIgnoreProperties("requests")
    private Trader trader;

    @ManyToOne
    @JoinColumn(name = "provider")
    private Provider provider;

    @ManyToOne
    @JoinColumn(name = "sourceCurrency")
    private Currency sourceCurrency;

    @ManyToOne
    @JoinColumn(name = "targetCurrency")
    private Currency targetCurrency;

    private String message;

    @ManyToOne
    @JoinColumn(name = "wallet")
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "bankAccount")
    private BankAccount bankAccount;
}
