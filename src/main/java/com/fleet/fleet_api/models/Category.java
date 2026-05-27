package com.fleet.fleet_api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 50 , unique = true)
    private String name;

    @OneToMany(mappedBy = "category" , fetch = FetchType.LAZY)
    private List<Asset> assets = new ArrayList<>();

}
