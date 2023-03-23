package com.demo.forexbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Trader {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Email
    @Column(nullable = false, unique = true)
    @NotBlank(message = "email field is required")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "name field is required")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "password field is required")
    private String password;

    @OneToMany(mappedBy = "trader")
    private List<BankAccount> bankAccounts;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet", referencedColumnName = "id")
    private Wallet wallet;

    @OneToMany(mappedBy = "trader")
    private List<Request> requests;
}
