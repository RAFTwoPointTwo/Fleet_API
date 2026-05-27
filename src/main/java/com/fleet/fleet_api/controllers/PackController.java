package com.fleet.fleet_api.controllers;

import com.fleet.fleet_api.dtos.packDTO.PackRequestDTO;
import com.fleet.fleet_api.dtos.packDTO.PackResponseDTO;
import com.fleet.fleet_api.services.PackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packs")
@RequiredArgsConstructor
public class PackController {

    private final PackService packService;

    @GetMapping
    public ResponseEntity<List<PackResponseDTO>> getPacks(){
        return ResponseEntity.ok(packService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackResponseDTO> getPackById(@PathVariable Long id){
        return ResponseEntity.ok(packService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PackResponseDTO>> searchPack(@RequestParam String label){
        return ResponseEntity.ok(packService.findByNameOrDescription(label));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getPacksCount(){
        return ResponseEntity.ok(packService.count());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PackResponseDTO> createPack(@Valid @RequestBody PackRequestDTO request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(packService.create(request));
    }

    @PostMapping("/{packId}/assets/add/{assetId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> addAsset(@PathVariable Long packId , @PathVariable Long assetId){
        packService.addAsset(packId , assetId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{packId}/assets/remove/{assetId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> removeAsset(@PathVariable Long packId , @PathVariable Long assetId){
        packService.removeAsset(packId , assetId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PackResponseDTO> updatePack(@PathVariable Long id , @RequestBody PackRequestDTO request){
        return ResponseEntity.ok(packService.update(id , request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deletePack(@PathVariable Long id){
        packService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
