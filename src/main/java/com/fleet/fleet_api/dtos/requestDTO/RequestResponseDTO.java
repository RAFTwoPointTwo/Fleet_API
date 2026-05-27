package com.fleet.fleet_api.dtos.requestDTO;

import com.fleet.fleet_api.dtos.assetDTO.AssetBaseInfoResponse;
import com.fleet.fleet_api.dtos.userDTO.UserBaseInfoResponseDTO;
import com.fleet.fleet_api.utilities.RequestStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RequestResponseDTO (
        Long id,
        String reason,
        RequestStatus status,
        LocalDate createdAt,
        String rejectReason,
        UserBaseInfoResponseDTO requester,
        UserBaseInfoResponseDTO validator,
        LocalDateTime validatedAt,
        AssetBaseInfoResponse asset,
        String packName
) {}