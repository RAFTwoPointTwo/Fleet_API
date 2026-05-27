package com.fleet.fleet_api.controllers;

import com.fleet.fleet_api.dtos.requestDTO.RequestCreateDTO;
import com.fleet.fleet_api.dtos.requestDTO.RequestResponseDTO;
import com.fleet.fleet_api.dtos.requestDTO.RequestValidationDTO;
import com.fleet.fleet_api.services.RequestService;
import com.fleet.fleet_api.utilities.RequestStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<RequestResponseDTO>> getRequests() {
        return ResponseEntity.ok(requestService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestResponseDTO> getRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<RequestResponseDTO>> searchRequest(@RequestParam String reason) {
        return ResponseEntity.ok(requestService.findByReason(reason));
    }

    @GetMapping("/requester/{requesterId}")
    public ResponseEntity<List<RequestResponseDTO>> getRequestsByRequesterId(@PathVariable Long requesterId) {
        return ResponseEntity.ok(requestService.findByRequesterId(requesterId));
    }

    @GetMapping("/requester/{requesterId}/status/{status}")
    public ResponseEntity<List<RequestResponseDTO>> getRequestsByRequesterIdAndStatus(@PathVariable Long requesterId, @PathVariable RequestStatus status) {
        return ResponseEntity.ok(requestService.findByRequesterIdAndStatus(requesterId, status));
    }

    @GetMapping("/validator/{validatorId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<RequestResponseDTO>> getRequestsByValidatorId(@PathVariable Long validatorId) {
        return ResponseEntity.ok(requestService.findByValidatorId(validatorId));
    }

    @GetMapping("/validator/{validatorId}/status/{status}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<RequestResponseDTO>> getRequestsByValidatorIdAndStatus(@PathVariable Long validatorId, @PathVariable RequestStatus status) {
        return ResponseEntity.ok(requestService.findByValidatorIdAndStatus(validatorId, status));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<RequestResponseDTO>> getRequestsByStatus(@PathVariable RequestStatus status) {
        return ResponseEntity.ok(requestService.findByStatus(status));
    }

    @GetMapping("/asset/{assetId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<RequestResponseDTO>> getRequestsByAssetId(@PathVariable Long assetId) {
        return ResponseEntity.ok(requestService.findByAssetId(assetId));
    }

    @GetMapping("/pack/{packId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<RequestResponseDTO>> getRequestsByPackId(@PathVariable Long packId) {
        return ResponseEntity.ok(requestService.findByPackId(packId));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getRequestsCount() {
        return ResponseEntity.ok(requestService.count());
    }

    @PostMapping("/asset")
    public ResponseEntity<RequestResponseDTO> createRequestForAsset(@Valid @RequestBody RequestCreateDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(requestService.createForAsset(request));
    }

    @PostMapping("/pack")
    public ResponseEntity<RequestResponseDTO> createRequestForPack(@Valid @RequestBody RequestCreateDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(requestService.createForPack(request));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelRequest(@PathVariable Long id, @RequestParam Long userId) {
        requestService.cancelRequest(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/validate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> validateRequest(@PathVariable Long id, @Valid @RequestBody RequestValidationDTO requestValidation) {
        requestService.validateRequest(id, requestValidation);
        return ResponseEntity.noContent().build();
    }
}