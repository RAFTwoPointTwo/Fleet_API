package com.fleet.fleet_api.dtos.breakdownDTO;

import com.fleet.fleet_api.dtos.assetDTO.AssetBaseInfoResponse;
import com.fleet.fleet_api.dtos.userDTO.UserBaseInfoResponseDTO;

import java.time.LocalDateTime;

public record BreakdownResponseDTO (
    Long id,
    String description,
    LocalDateTime reportedAt,
    AssetBaseInfoResponse brokenAsset,
    UserBaseInfoResponseDTO reporter
) {}
