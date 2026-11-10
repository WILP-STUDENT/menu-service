package com.mt03062.foodapp.menu;

import jakarta.persistence.*;

@Entity
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false)
    public Long restaurantId;
    @Column(nullable = false)
    public String name;
    public double price;
    public boolean available = true;
}
