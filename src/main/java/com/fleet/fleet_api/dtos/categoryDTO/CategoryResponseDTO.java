package com.fleet.fleet_api.dtos.categoryDTO;

public record CategoryResponseDTO (
    Long id,
    String name,
    long totalQuantity,
    long availableQuantity
) {}