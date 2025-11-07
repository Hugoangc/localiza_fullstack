package com.practice.localiza.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(name = "cart_item_acessory")
    private List<Acessory> chosenAccessories;

    @ManyToOne
    @JsonIgnore
    private Cart cart;

    @ManyToOne
    private Car car;

    private Double calculatedPrice;
}