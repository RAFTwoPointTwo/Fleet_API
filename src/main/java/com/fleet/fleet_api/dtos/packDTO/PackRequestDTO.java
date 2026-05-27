package com.fleet.fleet_api.dtos.packDTO;

import jakarta.validation.constraints.NotBlank;

public record PackRequestDTO (

    @NotBlank(message = "Veuillez renseigner le nom du pack")
    String name,

    @NotBlank(message = "Veuillez donner une description du pack")
    String description

) {}