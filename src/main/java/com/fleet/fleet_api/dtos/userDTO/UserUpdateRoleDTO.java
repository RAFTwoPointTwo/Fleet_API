package com.fleet.fleet_api.dtos.userDTO;

import com.fleet.fleet_api.utilities.UserRoles;
import jakarta.validation.constraints.NotNull;

public record UserUpdateRoleDTO (

    @NotNull(message = "Veuillez renseigner l'utilisateur sujet de la modification")
    Long userId,

    @NotNull(message = "Veuillez renseigner le nouveau rôle à attribuer")
    UserRoles newRole

) {}