package com.demo.forexbackend.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @SequenceGenerator(
            name = "request_id_sequence",
            sequenceName = "request_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "request_id_sequence"
    )
    private Long id;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(length = 32, columnDefinition = "varchar(32) default 'PENDING'")
    @Enumerated(value = EnumType.STRING)
    private Status status;

    private String message;
    private Double rate;

    @Column(nullable = false)
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "trader")
    private Trader trader;

    @ManyToOne
    @JoinColumn(name = "exchange")
    private Exchange exchange;

    @ManyToOne
    @JoinColumn(name = "sourceCurrency")
    private Currency sourceCurrency;

    @ManyToOne
    @JoinColumn(name = "targetCurrency")
    private Currency targetCurrency;


    @ManyToOne
    @JoinColumn(name = "wallet")
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "bankAccount")
    private BankAccount bankAccount;
}
