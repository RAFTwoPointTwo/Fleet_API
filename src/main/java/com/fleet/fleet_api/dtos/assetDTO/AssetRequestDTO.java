package com.fleet.fleet_api.dtos.assetDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AssetRequestDTO (

    @NotBlank(message = "Veuillez renseigner le nom de l'asset")
    String name,

    @NotBlank(message = "Veuillez renseigner le numéro de série de l'asset")
    String serialNumber,

    @NotNull(message = "Veuillez sélectionner une catégorie pour cet asset")
    Long categoryId

) {}
