package com.fleet.fleet_api.mappers;

import com.fleet.fleet_api.dtos.assignmentDTO.AssignmentRequestDTO;
import com.fleet.fleet_api.dtos.assignmentDTO.AssignmentResponseDTO;
import com.fleet.fleet_api.models.Assignment;
import com.fleet.fleet_api.models.Asset;
import com.fleet.fleet_api.models.Pack;
import com.fleet.fleet_api.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssignmentMapper implements Mapper<Assignment, AssignmentResponseDTO, AssignmentRequestDTO> {

    private final UserMapper userMapper;
    private final AssetMapper assetMapper;

    @Override
    public AssignmentResponseDTO toResponse(Assignment assignment) {
        return new AssignmentResponseDTO(
                assignment.getId(),
                assignment.getAssignedAsset() != null ? assetMapper.toBaseInfo(assignment.getAssignedAsset()) : null,
                assignment.getAssignedPack() != null ? assignment.getAssignedPack().getName() : null,
                assignment.getAssignedTo() != null ? userMapper.toBaseInfo(assignment.getAssignedTo()) : null,
                assignment.getStartDate(),
                assignment.getEndDate()
        );
    }

    @Override
    public Assignment toEntity(AssignmentRequestDTO request) {
        Assignment assignment = new Assignment();
        assignment.setStartDate(request.startDate());
        return assignment;
    }

    public Assignment toEntity(AssignmentRequestDTO request, Asset asset, Pack pack, User user) {
        Assignment assignment = this.toEntity(request);
        assignment.setAssignedAsset(asset);
        assignment.setAssignedPack(pack);
        assignment.setAssignedTo(user);
        return assignment;
    }
}