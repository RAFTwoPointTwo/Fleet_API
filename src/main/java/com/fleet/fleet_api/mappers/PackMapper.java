package com.fleet.fleet_api.mappers;

import com.fleet.fleet_api.dtos.packDTO.PackRequestDTO;
import com.fleet.fleet_api.dtos.packDTO.PackResponseDTO;
import com.fleet.fleet_api.models.Pack;
import com.fleet.fleet_api.utilities.PackStatus;
import org.springframework.stereotype.Component;

@Component
public class PackMapper implements Mapper<Pack, PackResponseDTO, PackRequestDTO> {

    @Override
    public PackResponseDTO toResponse(Pack pack) {
        return new PackResponseDTO(
                pack.getId(),
                pack.getName(),
                pack.getDescription(),
                pack.getStatus()
        );
    }

    @Override
    public Pack toEntity(PackRequestDTO request) {
        Pack pack = new Pack();
        pack.setName(request.name());
        pack.setDescription(request.description());
        pack.setStatus(PackStatus.AVAILABLE);
        return pack;
    }
}