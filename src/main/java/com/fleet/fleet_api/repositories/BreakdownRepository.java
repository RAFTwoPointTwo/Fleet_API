package com.fleet.fleet_api.repositories;

import com.fleet.fleet_api.models.Breakdown;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BreakdownRepository extends JpaRepository<Breakdown , Long> {

    List<Breakdown> findAllByOrderByReportedAtDesc();

    List<Breakdown> findByDescriptionContainsIgnoreCaseOrderByReportedAtDesc(String description);

    List<Breakdown> findByBrokenAssetIdOrderByReportedAtDesc(Long assetId);

}