package com.fleet.fleet_api.dtos.userDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDTO(

    @Email(message = "Veuillez renseigner un email valide")
    String email,

    @NotBlank(message = "Veuillez renseigner votre mot de passe")
    String password

) {}