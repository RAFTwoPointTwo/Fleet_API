package com.fleet.fleet_api.dtos.assetDTO;

import com.fleet.fleet_api.utilities.AssetStatus;

public record AssetResponseDTO (
        Long id,
        String name,
        String serialNumber,
        String categoryName,
        AssetStatus status
) {}
