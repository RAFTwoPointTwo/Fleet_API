package com.fleet.fleet_api.dtos.packDTO;

import com.fleet.fleet_api.utilities.PackStatus;

public record PackResponseDTO (
    Long id,
    String name,
    String description,
    PackStatus status
) {}