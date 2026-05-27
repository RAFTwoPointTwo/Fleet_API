package com.fleet.fleet_api.models;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "fleet_logs")
@Setter
@NoArgsConstructor
public class FleetLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , updatable = false)
    private String event;

    @CreationTimestamp
    @Column(nullable = false , updatable = false)
    private LocalDateTime timestamp;

}