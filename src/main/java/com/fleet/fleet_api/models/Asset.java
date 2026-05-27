package com.fleet.fleet_api.models;

import com.fleet.fleet_api.utilities.AssetStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assets")
@Getter
@Setter
@NoArgsConstructor
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 50)
    private String name;

    @Column(nullable = false , unique = true)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id" , nullable = false)
    private Category category;

    @ManyToMany(mappedBy = "assets" , fetch = FetchType.LAZY)
    private List<Pack> packs = new ArrayList<>();

    @OneToMany(mappedBy = "assignedAsset" , fetch = FetchType.LAZY)
    private List<Assignment> assignments = new ArrayList<>();

    @OneToMany(mappedBy = "brokenAsset" , fetch = FetchType.LAZY)
    private List<Breakdown> breakdowns = new ArrayList<>();

    @OneToMany(mappedBy = "asset" , fetch = FetchType.LAZY)
    private List<Maintenance> maintenances = new ArrayList<>();

    @OneToMany(mappedBy = "asset" , fetch = FetchType.LAZY)
    private List<Request> requests = new ArrayList<>();

}