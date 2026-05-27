package com.fleet.fleet_api.mappers;

import com.fleet.fleet_api.dtos.breakdownDTO.BreakdownRequestDTO;
import com.fleet.fleet_api.dtos.breakdownDTO.BreakdownResponseDTO;
import com.fleet.fleet_api.models.Breakdown;
import com.fleet.fleet_api.models.Asset;
import com.fleet.fleet_api.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BreakdownMapper implements Mapper<Breakdown, BreakdownResponseDTO, BreakdownRequestDTO> {

    private final UserMapper userMapper;
    private final AssetMapper assetMapper;

    @Override
    public BreakdownResponseDTO toResponse(Breakdown breakdown) {
        return new BreakdownResponseDTO(
                breakdown.getId(),
                breakdown.getDescription(),
                breakdown.getReportedAt(),
                breakdown.getBrokenAsset() != null ? assetMapper.toBaseInfo(breakdown.getBrokenAsset()) : null,
                breakdown.getReportedBy() != null ? userMapper.toBaseInfo(breakdown.getReportedBy()) : null
        );
    }

    @Override
    public Breakdown toEntity(BreakdownRequestDTO request) {
        Breakdown breakdown = new Breakdown();
        breakdown.setDescription(request.description());
        return breakdown;
    }

    public Breakdown toEntity(BreakdownRequestDTO request, Asset asset, User reporter) {
        Breakdown breakdown = this.toEntity(request);
        breakdown.setBrokenAsset(asset);
        breakdown.setReportedBy(reporter);
        return breakdown;
    }
}