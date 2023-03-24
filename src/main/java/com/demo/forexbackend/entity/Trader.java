package com.demo.forexbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trader {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Email
    @Column(nullable = false, unique = true)
    @NotBlank(message = "email field is required")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "name field is required")
    private String name;

    @OneToMany(mappedBy = "trader")
    private List<BankAccount> bankAccounts;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet", referencedColumnName = "id")
    private Wallet wallet;

    @OneToMany(mappedBy = "trader")
    private List<Request> requests;
}
