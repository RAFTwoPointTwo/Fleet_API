package com.fleet.fleet_api.controllers;

import com.fleet.fleet_api.dtos.breakdownDTO.BreakdownRequestDTO;
import com.fleet.fleet_api.dtos.breakdownDTO.BreakdownResponseDTO;
import com.fleet.fleet_api.services.BreakdownService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/breakdowns")
@RequiredArgsConstructor
public class BreakdownController {

    private final BreakdownService breakdownService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<BreakdownResponseDTO>> getBreakdowns() {
        return ResponseEntity.ok(breakdownService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BreakdownResponseDTO> getBreakdownById(@PathVariable Long id) {
        return ResponseEntity.ok(breakdownService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BreakdownResponseDTO>> searchBreakdown(@RequestParam String description) {
        return ResponseEntity.ok(breakdownService.findByDescription(description));
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<BreakdownResponseDTO>> getBreakdownsByAssetId(@PathVariable Long assetId) {
        return ResponseEntity.ok(breakdownService.findByBrokenAssetId(assetId));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getBreakdownsCount() {
        return ResponseEntity.ok(breakdownService.count());
    }

    @PostMapping
    public ResponseEntity<BreakdownResponseDTO> createBreakdown(@Valid @RequestBody BreakdownRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(breakdownService.create(request));
    }
}