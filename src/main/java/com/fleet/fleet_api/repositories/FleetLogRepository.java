package com.fleet.fleet_api.repositories;

import com.fleet.fleet_api.models.FleetLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FleetLogRepository extends JpaRepository<FleetLog , Long> {

    List<FleetLog> findAllByOrderByTimestamp();

}