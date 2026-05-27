package com.fleet.fleet_api.dtos.breakdownDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BreakdownRequestDTO (

    @NotBlank(message = "Veuillez renseigner une description pour cette panne")
    String description,

    @NotNull(message = "Veuillez renseigner l'asset endommagé")
    Long assetId,

    @NotNull(message = "Veuillez vous identifier")
    Long reporterId

) {}