package com.fleet.fleet_api.dtos.categoryDTO;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO (

    @NotBlank(message = "Veuillez renseigner le nom de cette catégorie")
    String name

) {}