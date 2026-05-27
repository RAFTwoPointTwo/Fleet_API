package com.fleet.fleet_api.services;

import com.fleet.fleet_api.dtos.assignmentDTO.AssignmentRequestDTO;
import com.fleet.fleet_api.dtos.assignmentDTO.AssignmentResponseDTO;
import com.fleet.fleet_api.exceptions.InvalidAssetStateException;
import com.fleet.fleet_api.exceptions.InvalidRequestParamException;
import com.fleet.fleet_api.exceptions.ResourceNotFoundException;
import com.fleet.fleet_api.mappers.AssignmentMapper;
import com.fleet.fleet_api.models.*;
import com.fleet.fleet_api.repositories.*;
import com.fleet.fleet_api.utilities.AssetStatus;
import com.fleet.fleet_api.utilities.PackStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentService {

    private final UserRepository userRepository;
    private final AssetRepository assetRepository;
    private final PackRepository packRepository;
    private final AssignmentRepository assignmentRepository;
    private final FleetLogRepository fleetLogRepository;
    private final AssignmentMapper assignmentMapper;

    public List<AssignmentResponseDTO> findAll(){
        return assignmentMapper.toResponseList(
                assignmentRepository.findAllByOrderByStartDateDesc()
        );
    }

    public AssignmentResponseDTO findById(Long id){
        return assignmentRepository.findById(id)
                .map(assignmentMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Cette assignation est introuvable"));
    }

    public List<AssignmentResponseDTO> findByAssignedAssetId(Long assetId){
        return assignmentMapper.toResponseList(
                assignmentRepository.findByAssignedAssetIdOrderByStartDateDesc(assetId)
        );
    }

    public List<AssignmentResponseDTO> findByAssignedPackId(Long packId){
        return assignmentMapper.toResponseList(
                assignmentRepository.findByAssignedPackIdOrderByStartDateDesc(packId)
        );
    }

    public List<AssignmentResponseDTO> findByRequesterId(Long requesterId){
        return assignmentMapper.toResponseList(
                assignmentRepository.findByAssignedToIdOrderByStartDateDesc(requesterId)
        );
    }

    @Transactional
    public AssignmentResponseDTO createForAsset(AssignmentRequestDTO request){
        if (request.assetId() == null){
            throw new InvalidRequestParamException("Veuillez renseigner un asset à affecter");
        }

        if (request.packId() != null){
            throw new InvalidRequestParamException("Veuillez renseigner un asset pour valider la demande");
        }

        if (request.requesterId() == null){
            throw new InvalidRequestParamException("Utilisateur introuvable");
        }

        User requester = userRepository.findById(request.requesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        Asset asset = assetRepository.findById(request.assetId())
                .orElseThrow(() -> new ResourceNotFoundException("Cet asset est introuvable"));

        if (asset.getStatus() != AssetStatus.AVAILABLE) {
            throw new InvalidAssetStateException("Cet asset n'est pas disponible");
        }

        asset.setStatus(AssetStatus.ASSIGNED);

        Assignment savedAssignment = assignmentRepository.save(
                assignmentMapper.toEntity(request , asset , null , requester)
        );

        FleetLog log = new FleetLog();
        log.setEvent("[ Enregistrement d'assignation ] " + asset.getName() + " assigné à " + requester.getFullName());
        fleetLogRepository.save(log);

        return assignmentMapper.toResponse(savedAssignment);
    }

    @Transactional
    public AssignmentResponseDTO createForPack(AssignmentRequestDTO request){
        if (request.packId() == null){
            throw new InvalidRequestParamException("Veuillez sélectionner un pack");
        }

        if (request.assetId() != null){
            throw new InvalidRequestParamException("Veuillez choisir un pack pour votre demande");
        }

        if (request.requesterId() == null){
            throw new InvalidRequestParamException("Veuillez vous identifier");
        }

        User requester = userRepository.findById(request.requesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        Pack pack = packRepository.findById(request.packId())
                .orElseThrow(() -> new ResourceNotFoundException("Ce pack est introuvable"));

        if (pack.getStatus() != PackStatus.AVAILABLE) {
            throw new InvalidRequestParamException("Ce pack n'est pas disponible");
        }

        pack.setStatus(PackStatus.ASSIGNED);

        Assignment savedAssignment = assignmentRepository.save(
                assignmentMapper.toEntity(request , null , pack , requester)
        );

        FleetLog log = new FleetLog();
        log.setEvent("[ Enregistrement d'assignation ] " + pack.getName() + " assigné à " + requester.getFullName());
        fleetLogRepository.save(log);

        return assignmentMapper.toResponse(savedAssignment);
    }

    @Transactional
    public void endAssignment(Long id){
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cette assignation est introuvable"));

        if (assignment.getEndDate() != null) {
            throw new InvalidRequestParamException("Cette assignation est déjà terminée");
        }

        if (assignment.getAssignedAsset() != null) {
            assignment.getAssignedAsset().setStatus(AssetStatus.AVAILABLE);
        }

        if (assignment.getAssignedPack() != null) {
            assignment.getAssignedPack().setStatus(PackStatus.AVAILABLE);
        }

        assignment.setEndDate(LocalDate.now());

        FleetLog log = new FleetLog();
        log.setEvent("[ Fin d'assignation ] Affectation à " + assignment.getAssignedTo().getFullName() + " terminée");
        fleetLogRepository.save(log);
    }

    public long count() {
        return assignmentRepository.count();
    }

}