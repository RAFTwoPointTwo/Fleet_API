package com.fleet.fleet_api.repositories;

import com.fleet.fleet_api.models.Pack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PackRepository extends JpaRepository<Pack , Long> {

    List<Pack> findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(String name , String description);

    boolean existsByIdAndAssetsId(Long packId , Long assetId);

    boolean existsByNameIgnoreCase(String name);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO packs_assets (pack_id, asset_id) VALUES (:packId, :assetId)", nativeQuery = true)
    void addAssetToPack(@Param("packId") Long packId, @Param("assetId") Long assetId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM packs_assets WHERE pack_id = :packId AND asset_id = :assetId", nativeQuery = true)
    void removeAssetFromPack(@Param("packId") Long packId, @Param("assetId") Long assetId);

}