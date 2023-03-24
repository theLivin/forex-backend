package com.demo.forexbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    @Id
    @Column(nullable = false)
    private String code;
    private String country;
    private String name;

    @OneToMany(mappedBy = "currency")
    private List<Exchange> exchanges;
}
