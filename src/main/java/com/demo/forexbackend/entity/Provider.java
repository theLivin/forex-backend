package com.demo.forexbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Provider {
    @Id
    private Long id;

    private String name;
    private String country;

    @OneToMany(mappedBy = "provider")
    private List<Exchange> exchanges;
}
