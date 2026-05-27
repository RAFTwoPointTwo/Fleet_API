package com.fleet.fleet_api.dtos.assignmentDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AssignmentRequestDTO (

    Long assetId,

    Long packId,

    @NotNull(message = "Veuillez vous identifier")
    Long requesterId,

    @NotNull(message = "La date d'attribution est requise")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate

) {}