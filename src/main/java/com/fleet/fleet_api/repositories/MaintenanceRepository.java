package com.fleet.fleet_api.repositories;

import com.fleet.fleet_api.models.Maintenance;
import com.fleet.fleet_api.utilities.MaintenanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance , Long> {

    List<Maintenance> findAllByOrderByStartDateDesc();

    List<Maintenance> findByDescriptionContainsIgnoreCaseOrderByStartDateDesc(String description);

    List<Maintenance> findByAssetIdOrderByStartDateDesc(Long assetId);

    List<Maintenance> findByResponsibleIdOrderByStartDateDesc(Long responsibleId);

    List<Maintenance> findByStatusOrderByStartDateDesc(MaintenanceStatus status);

}
