package com.fleet.fleet_api.dtos.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestCreateDTO (

    @NotBlank(message = "Veuillez renseigner le motif de votre demande")
    String reason,

    @NotNull(message = "Veuillez vous identifier")
    Long requesterId,

    Long assetId,

    Long packId

) {}