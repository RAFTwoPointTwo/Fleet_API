package com.fleet.fleet_api.controllers;

import com.fleet.fleet_api.dtos.assetDTO.AssetRequestDTO;
import com.fleet.fleet_api.dtos.assetDTO.AssetResponseDTO;
import com.fleet.fleet_api.services.AssetService;
import com.fleet.fleet_api.utilities.AssetStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @GetMapping
    public ResponseEntity<List<AssetResponseDTO>> getAssets() {
        return ResponseEntity.ok(assetService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetResponseDTO> getAssetById(@PathVariable Long id) {
        return ResponseEntity.ok(assetService.findById(id));
    }

    @GetMapping("/serial/{serialNumber}")
    public ResponseEntity<AssetResponseDTO> getAssetBySerialNumber(@PathVariable String serialNumber) {
        return ResponseEntity.ok(assetService.findBySerialNumber(serialNumber));
    }

    @GetMapping("/search")
    public ResponseEntity<List<AssetResponseDTO>> searchAsset(@RequestParam String name) {
        return ResponseEntity.ok(assetService.findByName(name));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AssetResponseDTO>> getAssetsByStatus(@PathVariable AssetStatus status) {
        return ResponseEntity.ok(assetService.findByStatus(status));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<AssetResponseDTO>> getAssetsByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(assetService.findByCategoryId(categoryId));
    }

    @GetMapping("/pack/{packId}")
    public ResponseEntity<List<AssetResponseDTO>> getAssetsByPackId(@PathVariable Long packId) {
        return ResponseEntity.ok(assetService.findByPackId(packId));
    }

    @GetMapping("/available")
    public ResponseEntity<List<AssetResponseDTO>> getAvailableAssets() {
        return ResponseEntity.ok(assetService.findAvailable());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getAssetsCount() {
        return ResponseEntity.ok(assetService.count());
    }

    @GetMapping("/count/{categoryId}")
    public ResponseEntity<Long> getAssetsCountByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(assetService.countByCategoryId(categoryId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AssetResponseDTO> createAsset(@Valid @RequestBody AssetRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assetService.create(request));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AssetResponseDTO> updateAsset(@PathVariable Long id, @Valid @RequestBody AssetRequestDTO request) {
        return ResponseEntity.ok(assetService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        assetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
