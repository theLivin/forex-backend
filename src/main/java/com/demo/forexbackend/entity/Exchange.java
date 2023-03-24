package com.demo.forexbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exchange extends Account{
    @ManyToOne
    @JoinColumn(name = "provider")
    private Provider provider;

    private Double rate;

    @OneToMany(mappedBy = "exchange")
    private List<Request> requests;
}
