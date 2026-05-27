package com.fleet.fleet_api.dtos.departmentDTO;

import jakarta.validation.constraints.NotBlank;

public record DepartmentRequestDTO (

    @NotBlank(message = "Veuillez rensigner les nom de ce département")
    String name

) {}