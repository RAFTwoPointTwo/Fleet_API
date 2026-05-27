package com.fleet.fleet_api.repositories;

import com.fleet.fleet_api.models.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment , Long> {

    List<Assignment> findAllByOrderByStartDateDesc();

    List<Assignment> findByAssignedToIdOrderByStartDateDesc(Long assignedToId);

    List<Assignment> findByAssignedAssetIdOrderByStartDateDesc(Long assignedAssetId);

    List<Assignment> findByAssignedPackIdOrderByStartDateDesc(Long assignedPackId);

}
