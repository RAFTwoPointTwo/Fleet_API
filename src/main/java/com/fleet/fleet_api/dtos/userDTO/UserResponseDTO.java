package com.fleet.fleet_api.dtos.userDTO;

import com.fleet.fleet_api.utilities.UserRoles;

public record UserResponseDTO (
    Long id,
    String lastName,
    String firstNames,
    String email,
    UserRoles role,
    String departmentName
) {}