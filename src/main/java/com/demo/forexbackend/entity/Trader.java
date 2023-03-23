package com.demo.forexbackend.entity;

import jakarta.persistence.*;

@Entity
public class Trader {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }
}
