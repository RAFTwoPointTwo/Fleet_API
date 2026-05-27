package com.fleet.fleet_api.controllers;

import com.fleet.fleet_api.dtos.assignmentDTO.AssignmentRequestDTO;
import com.fleet.fleet_api.dtos.assignmentDTO.AssignmentResponseDTO;
import com.fleet.fleet_api.services.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignments() {
        return ResponseEntity.ok(assignmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentResponseDTO> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.findById(id));
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByAssetId(@PathVariable Long assetId) {
        return ResponseEntity.ok(assignmentService.findByAssignedAssetId(assetId));
    }

    @GetMapping("/pack/{packId}")
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByPackId(@PathVariable Long packId) {
        return ResponseEntity.ok(assignmentService.findByAssignedPackId(packId));
    }

    @GetMapping("/requester/{requesterId}")
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByRequesterId(@PathVariable Long requesterId) {
        return ResponseEntity.ok(assignmentService.findByRequesterId(requesterId));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getAssignmentsCount() {
        return ResponseEntity.ok(assignmentService.count());
    }

    @PostMapping("/asset")
    public ResponseEntity<AssignmentResponseDTO> createAssignmentForAsset(@Valid @RequestBody AssignmentRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assignmentService.createForAsset(request));
    }

    @PostMapping("/pack")
    public ResponseEntity<AssignmentResponseDTO> createAssignmentForPack(@Valid @RequestBody AssignmentRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assignmentService.createForPack(request));
    }

    @PatchMapping("/{id}/end")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> endAssignment(@PathVariable Long id) {
        assignmentService.endAssignment(id);
        return ResponseEntity.noContent().build();
    }
}