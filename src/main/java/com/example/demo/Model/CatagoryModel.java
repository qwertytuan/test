package com.example.demo.Model;

import jakarta.persistence.*;

@Entity
public class CatagoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

}
