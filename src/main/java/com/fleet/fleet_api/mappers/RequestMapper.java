package com.fleet.fleet_api.mappers;

import com.fleet.fleet_api.dtos.requestDTO.RequestCreateDTO;
import com.fleet.fleet_api.dtos.requestDTO.RequestResponseDTO;
import com.fleet.fleet_api.models.Request;
import com.fleet.fleet_api.models.User;
import com.fleet.fleet_api.models.Asset;
import com.fleet.fleet_api.models.Pack;
import com.fleet.fleet_api.utilities.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestMapper implements Mapper<Request, RequestResponseDTO, RequestCreateDTO> {

    private final UserMapper userMapper;
    private final AssetMapper assetMapper;

    @Override
    public RequestResponseDTO toResponse(Request request) {
        return new RequestResponseDTO(
                request.getId(),
                request.getReason(),
                request.getStatus(),
                request.getCreatedAt() != null ? request.getCreatedAt().toLocalDate() : null,
                request.getRejectReason(),
                request.getRequester() != null ? userMapper.toBaseInfo(request.getRequester()) : null,
                request.getValidator() !=null ? userMapper.toBaseInfo(request.getValidator()) : null,
                request.getValidatedAt(),
                request.getAsset() != null ? assetMapper.toBaseInfo(request.getAsset()) : null,
                request.getPack() != null ? request.getPack().getName() : null
        );
    }

    @Override
    public Request toEntity(RequestCreateDTO dto) {
        Request request = new Request();
        request.setReason(dto.reason());
        request.setStatus(RequestStatus.PENDING);
        return request;
    }

    public Request toEntity(RequestCreateDTO dto, User requester, Asset asset, Pack pack) {
        Request request = this.toEntity(dto);
        request.setRequester(requester);
        request.setAsset(asset);
        request.setPack(pack);
        return request;
    }
}