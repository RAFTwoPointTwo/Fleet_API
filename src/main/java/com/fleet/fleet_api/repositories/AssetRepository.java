package com.fleet.fleet_api.repositories;

import com.fleet.fleet_api.models.Asset;
import com.fleet.fleet_api.utilities.AssetStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset , Long> {

    Optional<Asset> findBySerialNumber(String serialNumber);

    List<Asset> findByNameContainsIgnoreCase(String name);

    List<Asset> findByStatus(AssetStatus status);

    List<Asset> findByCategoryId(Long categoryId);

    List<Asset> findByPacksId(Long packId);

    boolean existsBySerialNumber(String serialNumber);

    long countByCategoryId(Long categoryId);

}