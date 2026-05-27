package com.fleet.fleet_api.controllers;

import com.fleet.fleet_api.dtos.maintenanceDTO.MaintenanceRequestDTO;
import com.fleet.fleet_api.dtos.maintenanceDTO.MaintenanceResponseDTO;
import com.fleet.fleet_api.services.MaintenanceService;
import com.fleet.fleet_api.utilities.MaintenanceStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenances")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenances() {
        return ResponseEntity.ok(maintenanceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDTO> getMaintenanceById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MaintenanceResponseDTO>> searchMaintenance(@RequestParam String description) {
        return ResponseEntity.ok(maintenanceService.findByDescription(description));
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenancesByAssetId(@PathVariable Long assetId) {
        return ResponseEntity.ok(maintenanceService.findByAssetId(assetId));
    }

    @GetMapping("/responsible/{responsibleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenancesByResponsibleId(@PathVariable Long responsibleId) {
        return ResponseEntity.ok(maintenanceService.findByResponsibleId(responsibleId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenancesByStatus(@PathVariable MaintenanceStatus status) {
        return ResponseEntity.ok(maintenanceService.findByStatus(status));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getMaintenancesCount() {
        return ResponseEntity.ok(maintenanceService.count());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MaintenanceResponseDTO> createMaintenance(@Valid @RequestBody MaintenanceRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(maintenanceService.create(request));
    }

    @PatchMapping("/{id}/end")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> endMaintenance(@PathVariable Long id) {
        maintenanceService.endMaintenance(id);
        return ResponseEntity.noContent().build();
    }
}
