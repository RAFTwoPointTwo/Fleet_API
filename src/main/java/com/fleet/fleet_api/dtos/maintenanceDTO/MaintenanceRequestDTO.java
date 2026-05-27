package com.fleet.fleet_api.dtos.maintenanceDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MaintenanceRequestDTO (

    String description,

    @NotNull(message = "Veuillez renseigner l'asset à maintenir")
    Long assetId,

    @NotNull(message = "Veuillez renseigner la panne subie")
    Long breakdownId,

    @NotNull(message = "Veuillez vous identifier")
    Long responsibleId,

    @NotNull(message = "Veuillez renseigner la date du début de maintenance")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate

) {}
