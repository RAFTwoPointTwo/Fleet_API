package com.fleet.fleet_api.mappers;

import com.fleet.fleet_api.dtos.maintenanceDTO.MaintenanceRequestDTO;
import com.fleet.fleet_api.dtos.maintenanceDTO.MaintenanceResponseDTO;
import com.fleet.fleet_api.models.Maintenance;
import com.fleet.fleet_api.models.Asset;
import com.fleet.fleet_api.models.Breakdown;
import com.fleet.fleet_api.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaintenanceMapper implements Mapper<Maintenance, MaintenanceResponseDTO, MaintenanceRequestDTO> {

    private final AssetMapper assetMapper;
    private final UserMapper userMapper;

    @Override
    public MaintenanceResponseDTO toResponse(Maintenance maintenance) {
        return new MaintenanceResponseDTO(
                maintenance.getId(),
                maintenance.getDescription(),
                maintenance.getAsset() != null ? assetMapper.toBaseInfo(maintenance.getAsset()) : null,
                maintenance.getResponsible() != null ? userMapper.toBaseInfo(maintenance.getResponsible()) : null,
                maintenance.getStatus(),
                maintenance.getStartDate(),
                maintenance.getEndDate()
        );
    }

    @Override
    public Maintenance toEntity(MaintenanceRequestDTO request) {
        Maintenance maintenance = new Maintenance();
        maintenance.setDescription(request.description());
        maintenance.setStartDate(request.startDate());
        return maintenance;
    }

    public Maintenance toEntity(MaintenanceRequestDTO request, Asset asset, Breakdown breakdown, User responsible) {
        Maintenance maintenance = this.toEntity(request);
        maintenance.setAsset(asset);
        maintenance.setBreakdown(breakdown);
        maintenance.setResponsible(responsible);
        return maintenance;
    }
}