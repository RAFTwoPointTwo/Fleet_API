package com.fleet.fleet_api.dtos.requestDTO;

import jakarta.validation.constraints.NotNull;

public record RequestValidationDTO(

    @NotNull(message = "Veuillez vous identifier")
    Long validatorId,

    @NotNull(message = "Veuillez préciser votre décision, quant à la demande")
    Boolean validated,

    String rejectReason

) {}