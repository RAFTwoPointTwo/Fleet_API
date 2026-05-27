package com.fleet.fleet_api.controllers;

import com.fleet.fleet_api.models.FleetLog;
import com.fleet.fleet_api.services.FleetLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class FleetLogController {

    private final FleetLogService fleetLogService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<FleetLog>> getLogs(){
        return ResponseEntity.ok(fleetLogService.findAllLogs());
    }

}