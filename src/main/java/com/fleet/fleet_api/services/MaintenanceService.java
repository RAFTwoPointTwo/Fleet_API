package com.fleet.fleet_api.services;


import com.fleet.fleet_api.dtos.maintenanceDTO.MaintenanceRequestDTO;
import com.fleet.fleet_api.dtos.maintenanceDTO.MaintenanceResponseDTO;
import com.fleet.fleet_api.exceptions.InvalidAssetStateException;
import com.fleet.fleet_api.exceptions.InvalidRequestParamException;
import com.fleet.fleet_api.exceptions.ResourceNotFoundException;
import com.fleet.fleet_api.mappers.MaintenanceMapper;
import com.fleet.fleet_api.models.*;
import com.fleet.fleet_api.repositories.*;
import com.fleet.fleet_api.utilities.AssetStatus;
import com.fleet.fleet_api.utilities.MaintenanceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final BreakdownRepository breakdownRepository;
    private final FleetLogRepository fleetLogRepository;
    private final MaintenanceMapper maintenanceMapper;

    public List<MaintenanceResponseDTO> findAll() {
        return maintenanceMapper.toResponseList(
                maintenanceRepository.findAllByOrderByStartDateDesc()
        );
    }

    public MaintenanceResponseDTO findById(Long id) {
        return maintenanceRepository.findById(id)
                .map(maintenanceMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Cette maintenance est introuvable"));
    }

    public List<MaintenanceResponseDTO> findByDescription(String description) {
        return maintenanceMapper.toResponseList(
                maintenanceRepository.findByDescriptionContainsIgnoreCaseOrderByStartDateDesc(description)
        );
    }

    public List<MaintenanceResponseDTO> findByAssetId(Long assetId) {
        return maintenanceMapper.toResponseList(
                maintenanceRepository.findByAssetIdOrderByStartDateDesc(assetId)
        );
    }

    public List<MaintenanceResponseDTO> findByResponsibleId(Long responsibleId) {
        return maintenanceMapper.toResponseList(
                maintenanceRepository.findByResponsibleIdOrderByStartDateDesc(responsibleId)
        );
    }

    public List<MaintenanceResponseDTO> findByStatus(MaintenanceStatus status) {
        return maintenanceMapper.toResponseList(
                maintenanceRepository.findByStatusOrderByStartDateDesc(status)
        );
    }

    @Transactional
    public MaintenanceResponseDTO create(MaintenanceRequestDTO request) {
        if (request.assetId() == null){
            throw new InvalidRequestParamException("Veuillez renseigner l'asset à mettre en maintenance");
        }

        if (request.breakdownId() == null){
            throw new InvalidRequestParamException("Veuillez renseigner la panne subvenue");
        }

        if (request.responsibleId() == null){
            throw new InvalidRequestParamException("Veuillez renseigner le responsable de maintenance");
        }

        if (request.startDate() == null){
            throw new InvalidRequestParamException("Veuillez renseigner la date de début de maintenance");
        }

        Asset asset = assetRepository.findById(request.assetId())
                .orElseThrow(() -> new ResourceNotFoundException("Cet asset est introuvable"));

        if (asset.getStatus() == AssetStatus.UNDER_MAINTENANCE) {
            throw new InvalidAssetStateException("Cet asset est déjà en maintenance");
        }

        User responsible = userRepository.findById(request.responsibleId())
                .orElseThrow(() -> new ResourceNotFoundException("Cet utilisateur est introuvable"));

        Breakdown breakdown = breakdownRepository.findById(request.breakdownId())
                .orElseThrow(() -> new ResourceNotFoundException("Cette panne est introuvable"));

        asset.setStatus(AssetStatus.UNDER_MAINTENANCE);

        Maintenance savedMaintenance = maintenanceRepository.save(
                maintenanceMapper.toEntity(request, asset, breakdown, responsible)
        );

        FleetLog log = new FleetLog();
        log.setEvent("[ Enregistrement de maintenance ] " + savedMaintenance.getAsset().getName() + " en maintenance");
        fleetLogRepository.save(log);

        return maintenanceMapper.toResponse(savedMaintenance);
    }

    @Transactional
    public void endMaintenance(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cette maintenance est introuvable"));

        if (maintenance.getEndDate() != null) {
            throw new InvalidRequestParamException("Cette maintenance est déjà terminée");
        }

        maintenance.setEndDate(LocalDate.now());

        maintenance.getAsset().setStatus(AssetStatus.AVAILABLE);

        maintenance.setStatus(MaintenanceStatus.FINISHED);

        FleetLog log = new FleetLog();
        log.setEvent("[ Fin de maintenance ] Maintenance de " + maintenance.getAsset().getName() + " terminée");
        fleetLogRepository.save(log);
    }

    public long count(){
        return maintenanceRepository.count();
    }

}