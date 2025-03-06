package com.E_Commer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String street;

    private String city;
    private String state;
    private String country;
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
}
