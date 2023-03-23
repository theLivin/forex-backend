package com.demo.forexbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Currency {
    @Id
    @Column(nullable = false)
    private String code;
    private String country;

    @OneToMany(mappedBy = "currency")
    private List<Exchange> exchanges;
}
