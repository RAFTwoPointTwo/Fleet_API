package com.fleet.fleet_api.dtos.assignmentDTO;

import com.fleet.fleet_api.dtos.assetDTO.AssetBaseInfoResponse;
import com.fleet.fleet_api.dtos.userDTO.UserBaseInfoResponseDTO;

import java.time.LocalDate;

public record AssignmentResponseDTO (
    Long id,
    AssetBaseInfoResponse assignedAsset,
    String assignedPackName,
    UserBaseInfoResponseDTO requester,
    LocalDate startDate,
    LocalDate endDate
) {}