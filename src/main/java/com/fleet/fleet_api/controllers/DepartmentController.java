package com.fleet.fleet_api.controllers;

import com.fleet.fleet_api.dtos.departmentDTO.DepartmentRequestDTO;
import com.fleet.fleet_api.dtos.departmentDTO.DepartmentResponseDTO;
import com.fleet.fleet_api.services.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    public final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getDepartments(){
        return ResponseEntity.ok(departmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable Long id){
        return ResponseEntity.ok(departmentService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DepartmentResponseDTO>> searchDepartment(@RequestParam String name){
        return ResponseEntity.ok(departmentService.findByName(name));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@Valid @RequestBody DepartmentRequestDTO request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(departmentService.create(request));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(@PathVariable Long id , @Valid @RequestBody DepartmentRequestDTO request){
        return ResponseEntity.ok(departmentService.update(id , request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id){
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}