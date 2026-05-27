package com.fleet.fleet_api.dtos.maintenanceDTO;

import com.fleet.fleet_api.dtos.assetDTO.AssetBaseInfoResponse;
import com.fleet.fleet_api.dtos.userDTO.UserBaseInfoResponseDTO;
import com.fleet.fleet_api.utilities.MaintenanceStatus;

import java.time.LocalDate;

public record MaintenanceResponseDTO (
    Long id,
    String description,
    AssetBaseInfoResponse asset,
    UserBaseInfoResponseDTO responsible,
    MaintenanceStatus status,
    LocalDate startDate,
    LocalDate endDate
) {}