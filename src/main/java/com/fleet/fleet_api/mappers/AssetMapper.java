package com.fleet.fleet_api.mappers;

import com.fleet.fleet_api.dtos.assetDTO.AssetBaseInfoResponse;
import com.fleet.fleet_api.dtos.assetDTO.AssetRequestDTO;
import com.fleet.fleet_api.dtos.assetDTO.AssetResponseDTO;
import com.fleet.fleet_api.models.Asset;
import com.fleet.fleet_api.models.Category;
import com.fleet.fleet_api.utilities.AssetStatus;
import org.springframework.stereotype.Component;

@Component
public class AssetMapper implements Mapper<Asset, AssetResponseDTO, AssetRequestDTO> {

    @Override
    public AssetResponseDTO toResponse(Asset asset) {
        return new AssetResponseDTO(
                asset.getId(),
                asset.getName(),
                asset.getSerialNumber(),
                asset.getCategory() != null ? asset.getCategory().getName() : null,
                asset.getStatus()
        );
    }

    @Override
    public Asset toEntity(AssetRequestDTO request) {
        Asset asset = new Asset();
        asset.setName(request.name());
        asset.setSerialNumber(request.serialNumber());
        asset.setStatus(AssetStatus.AVAILABLE);
        return asset;
    }

    public Asset toEntity(AssetRequestDTO request, Category category) {
        Asset asset = this.toEntity(request);
        asset.setCategory(category);
        return asset;
    }

    public AssetBaseInfoResponse toBaseInfo(Asset asset) {
        return new AssetBaseInfoResponse(
                asset.getName(),
                asset.getCategory() != null ? asset.getCategory().getName() : null
        );
    }
}