package com.fleet.fleet_api.dtos.userDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserUpdateProfileDTO (

        @NotBlank(message = "Veuillez renseigner votre nom")
        String lastName,

        @NotBlank(message = "Veuillez renseigner vos prénoms")
        String firstNames,

        @NotNull(message = "Veuillez renseigner votre département")
        Long departmentId

)
{}