package com.fleet.fleet_api.models;

import com.fleet.fleet_api.utilities.UserRoles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 50)
    private String lastName;

    @Column(nullable = false , length = 50)
    private String firstNames;

    @Column(nullable = false , unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoles role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id" , nullable = false)
    private Department department;

    @OneToMany(mappedBy = "assignedTo" , fetch = FetchType.LAZY)
    private List<Assignment> assignments = new ArrayList<>();

    @OneToMany(mappedBy = "reportedBy" , fetch = FetchType.LAZY)
    private List<Breakdown> breakdowns = new ArrayList<>();

    @OneToMany(mappedBy = "requester" , fetch = FetchType.LAZY)
    private List<Request> requests = new ArrayList<>();

    @OneToMany(mappedBy = "validator" , fetch = FetchType.LAZY)
    private List<Request> validatedRequests = new ArrayList<>();


    public String getFullName() {
        return firstNames + " " + lastName;
    }

}