package com.fleet.fleet_api.services;

import com.fleet.fleet_api.dtos.requestDTO.RequestCreateDTO;
import com.fleet.fleet_api.dtos.requestDTO.RequestResponseDTO;
import com.fleet.fleet_api.dtos.requestDTO.RequestValidationDTO;
import com.fleet.fleet_api.exceptions.InvalidAssetStateException;
import com.fleet.fleet_api.exceptions.InvalidRequestParamException;
import com.fleet.fleet_api.exceptions.ResourceNotFoundException;
import com.fleet.fleet_api.mappers.RequestMapper;
import com.fleet.fleet_api.models.*;
import com.fleet.fleet_api.repositories.*;
import com.fleet.fleet_api.utilities.AssetStatus;
import com.fleet.fleet_api.utilities.PackStatus;
import com.fleet.fleet_api.utilities.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestService {

    private final RequestRepository requestRepository;
    private final AssetRepository assetRepository;
    private final PackRepository packRepository;
    private final UserRepository userRepository;
    private final FleetLogRepository fleetLogRepository;
    private final RequestMapper requestMapper;

    public List<RequestResponseDTO> findAll(){
        return requestMapper.toResponseList(
                requestRepository.findAllByOrderByCreatedAtDesc()
        );
    }

    public RequestResponseDTO findById(Long id){
        return requestRepository.findById(id)
                .map(requestMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Cette demande est introuvable"));
    }

    public List<RequestResponseDTO> findByReason(String reason){
        return requestMapper.toResponseList(
                requestRepository.findByReasonContainsIgnoreCaseOrderByCreatedAtDesc(reason)
        );
    }

    public List<RequestResponseDTO> findByRequesterId(Long requesterId){
        return requestMapper.toResponseList(
                requestRepository.findByRequesterIdOrderByCreatedAtDesc(requesterId)
        );
    }

    public List<RequestResponseDTO> findByRequesterIdAndStatus(Long requesterId, RequestStatus status){
        return requestMapper.toResponseList(
                requestRepository.findByRequesterIdAndStatusOrderByCreatedAtDesc(requesterId, status)
        );
    }

    public List<RequestResponseDTO> findByValidatorId(Long validatorId){
        return requestMapper.toResponseList(
                requestRepository.findByValidatorIdOrderByCreatedAtDesc(validatorId)
        );
    }

    public List<RequestResponseDTO> findByValidatorIdAndStatus(Long validatorId, RequestStatus status){
        return requestMapper.toResponseList(
                requestRepository.findByValidatorIdAndStatusOrderByCreatedAtDesc(validatorId, status)
        );
    }

    public List<RequestResponseDTO> findByStatus(RequestStatus status){
        return requestMapper.toResponseList(
                requestRepository.findByStatusOrderByCreatedAtDesc(status)
        );
    }

    public List<RequestResponseDTO> findByAssetId(Long assetId){
        return requestMapper.toResponseList(
                requestRepository.findByAssetIdOrderByCreatedAtDesc(assetId)
        );
    }

    public List<RequestResponseDTO> findByPackId(Long packId){
        return requestMapper.toResponseList(
                requestRepository.findByPackIdOrderByCreatedAtDesc(packId)
        );
    }

    @Transactional
    public RequestResponseDTO createForAsset(RequestCreateDTO request){
        if (request.reason() == null){
            throw new InvalidRequestParamException("Veuillez renseigner la raison de votre requête");
        }

        if (request.requesterId() == null){
            throw new InvalidRequestParamException("Veuillez vous identifier");
        }

        if (request.assetId() == null){
            throw new InvalidRequestParamException("Veuillez choisir un asset pour votre demande");
        }

        if (request.packId() != null){
            throw new InvalidRequestParamException("Veuillez choisir un asset pour votre demande");
        }

        User requester = userRepository.findById(request.requesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        Asset asset = assetRepository.findById(request.assetId())
                .orElseThrow(() -> new ResourceNotFoundException("Cet asset est introuvable"));

        if (asset.getStatus() != AssetStatus.AVAILABLE) {
            throw new InvalidAssetStateException("Cet asset n'est pas disponible");
        }

        Request savedRequest = requestRepository.save(
                requestMapper.toEntity(request , requester , asset , null)
        );

        FleetLog log = new FleetLog();
        log.setEvent("[ Enregistrement de requête ] Requête pour < " + savedRequest.getReason() + " >");
        fleetLogRepository.save(log);

        return requestMapper.toResponse(savedRequest);
    }

    @Transactional
    public RequestResponseDTO createForPack(RequestCreateDTO request){
        if (request.reason() == null){
            throw new InvalidRequestParamException("Veuillez renseigner la raison de votre requête");
        }

        if (request.requesterId() == null){
            throw new InvalidRequestParamException("Veuillez vous identifier");
        }

        if (request.packId() == null){
            throw new InvalidRequestParamException("Veuillez choisir un pack pour votre demande");
        }

        if (request.assetId() != null){
            throw new InvalidRequestParamException("Veuillez choisir un pack pour votre demande");
        }

        User requester = userRepository.findById(request.requesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        Pack pack = packRepository.findById(request.packId())
                .orElseThrow(() -> new ResourceNotFoundException("Ce pack est introuvable"));

        if (pack.getStatus() != PackStatus.AVAILABLE) {
            throw new InvalidRequestParamException("Ce pack n'est pas disponible");
        }

        Request savedRequest = requestRepository.save(
                requestMapper.toEntity(request , requester , null , pack)
        );

        FleetLog log = new FleetLog();
        log.setEvent("[ Enregistrement de requête ] Requête pour < " + savedRequest.getReason() + " >");
        fleetLogRepository.save(log);

        return requestMapper.toResponse(savedRequest);
    }

    @Transactional
    public void cancelRequest(Long id , Long userId){
        Request userRequest = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cette requête est introuvable"));

        if (userRequest.getStatus() != RequestStatus.PENDING){
            throw new InvalidRequestParamException("Cette requête a déjà été traitée");
        }

        if (!Objects.equals(userRequest.getRequester().getId(), userId)){
            throw new InvalidRequestParamException("La requête ne peut être supprimée que par son initiateur");
        }

        userRequest.setStatus(RequestStatus.CANCELED);

        FleetLog log = new FleetLog();
        log.setEvent("[ Traitement de requête ] Requête pour < " + userRequest.getReason() + " > traitée. Statut : " + userRequest.getStatus().name());
        fleetLogRepository.save(log);
    }

    @Transactional
    public void validateRequest(Long id , RequestValidationDTO requestValidation){
        Request userRequest = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cette requête est introuvable"));

        if (userRequest.getStatus() != RequestStatus.PENDING){
            throw new InvalidRequestParamException("Cette requête a déjà été traitée");
        }

        User validator = userRepository.findById(requestValidation.validatorId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        if (requestValidation.validated()){
            userRequest.setStatus(RequestStatus.APPROVED);
        }else {
            if (requestValidation.rejectReason() == null){
                throw new InvalidRequestParamException("Veuillez donner la raison de votre rejet");
            }
            userRequest.setStatus(RequestStatus.REJECTED);
            userRequest.setRejectReason(requestValidation.rejectReason());
        }

        userRequest.setValidator(validator);

        userRequest.setValidatedAt(LocalDateTime.now());

        FleetLog log = new FleetLog();
        log.setEvent("[ Traitement de requête ] Requête pour < " + userRequest.getReason() + " > traitée. Statut : " + userRequest.getStatus().name());
        fleetLogRepository.save(log);
    }

    public long count(){
        return requestRepository.count();
    }

}