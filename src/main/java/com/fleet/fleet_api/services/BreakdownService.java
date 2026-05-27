package com.fleet.fleet_api.services;

import com.fleet.fleet_api.dtos.breakdownDTO.BreakdownRequestDTO;
import com.fleet.fleet_api.dtos.breakdownDTO.BreakdownResponseDTO;
import com.fleet.fleet_api.exceptions.InvalidAssetStateException;
import com.fleet.fleet_api.exceptions.InvalidRequestParamException;
import com.fleet.fleet_api.exceptions.ResourceNotFoundException;
import com.fleet.fleet_api.mappers.BreakdownMapper;
import com.fleet.fleet_api.models.Asset;
import com.fleet.fleet_api.models.Breakdown;
import com.fleet.fleet_api.models.FleetLog;
import com.fleet.fleet_api.models.User;
import com.fleet.fleet_api.repositories.AssetRepository;
import com.fleet.fleet_api.repositories.BreakdownRepository;
import com.fleet.fleet_api.repositories.FleetLogRepository;
import com.fleet.fleet_api.repositories.UserRepository;
import com.fleet.fleet_api.utilities.AssetStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BreakdownService {

    private final BreakdownRepository breakdownRepository;
    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final FleetLogRepository fleetLogRepository;
    private final BreakdownMapper breakdownMapper;

    public List<BreakdownResponseDTO> findAll(){
        return breakdownMapper.toResponseList(
                breakdownRepository.findAllByOrderByReportedAtDesc()
        );
    }

    public BreakdownResponseDTO findById(Long id){
        return breakdownRepository.findById(id)
                .map(breakdownMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Cette pane est introuvable"));
    }

    public List<BreakdownResponseDTO> findByDescription(String description){
        return breakdownMapper.toResponseList(
                breakdownRepository.findByDescriptionContainsIgnoreCaseOrderByReportedAtDesc(description)
        );
    }

    public List<BreakdownResponseDTO> findByBrokenAssetId(Long assetId){
        return breakdownMapper.toResponseList(
                breakdownRepository.findByBrokenAssetIdOrderByReportedAtDesc(assetId)
        );
    }

    @Transactional
    public BreakdownResponseDTO create(BreakdownRequestDTO request){
        if (request.description() == null){
            throw new InvalidRequestParamException("Veuillez donner une description pour cette panne");
        }

        if (request.assetId() == null){
            throw new InvalidRequestParamException("Veuillez renseigner l'asset endommagé");
        }

        if (request.reporterId() == null){
            throw new InvalidRequestParamException("Veuillez vous identifier");
        }

        Asset asset = assetRepository.findById(request.assetId())
                .orElseThrow(() -> new ResourceNotFoundException("L'asset mentionné est introuvable"));

        if (asset.getStatus() == AssetStatus.OUT_OF_SERVICE) {
            throw new InvalidAssetStateException("Cet asset est déjà hors service");
        }

        asset.setStatus(AssetStatus.OUT_OF_SERVICE);

        User reporter = userRepository.findById(request.reporterId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        Breakdown savedBreakdown = breakdownRepository.save(
                breakdownMapper.toEntity(request , asset , reporter)
        );

        FleetLog log = new FleetLog();
        log.setEvent("[ Enregistrement de panne ] Panne subvenue sur " + asset.getName());
        fleetLogRepository.save(log);

        return breakdownMapper.toResponse(savedBreakdown);

    }

    public long count(){
        return breakdownRepository.count();
    }

}