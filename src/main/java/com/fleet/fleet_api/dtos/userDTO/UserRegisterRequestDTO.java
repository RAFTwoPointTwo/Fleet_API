package com.fleet.fleet_api.dtos.userDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRegisterRequestDTO(

    @NotBlank(message = "Veuillez renseigner votre nom")
    String lastName,

    @NotBlank(message = "Veuillez renseigner vos prénoms")
    String firstNames,

    @Email(message = "Veuillez renseigner un email valide")
    @NotBlank(message = "Veuillez renseigner un email valide")
    String email,

    @NotBlank(message = "Veuillez renseigner un mot de passe")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    String password,

    @NotNull(message = "Veuillez renseigner votre département")
    Long departmentId

) {}