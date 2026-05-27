package com.fleet.fleet_api.services;

import com.fleet.fleet_api.dtos.packDTO.PackRequestDTO;
import com.fleet.fleet_api.dtos.packDTO.PackResponseDTO;
import com.fleet.fleet_api.exceptions.DuplicateResourceException;
import com.fleet.fleet_api.exceptions.InvalidRequestParamException;
import com.fleet.fleet_api.exceptions.ResourceNotFoundException;
import com.fleet.fleet_api.mappers.PackMapper;
import com.fleet.fleet_api.models.FleetLog;
import com.fleet.fleet_api.models.Pack;
import com.fleet.fleet_api.repositories.AssetRepository;
import com.fleet.fleet_api.repositories.FleetLogRepository;
import com.fleet.fleet_api.repositories.PackRepository;
import com.fleet.fleet_api.utilities.PackStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PackService {

    private final AssetRepository assetRepository;
    private final PackRepository packRepository;
    private final FleetLogRepository fleetLogRepository;
    private final PackMapper packMapper;

    public List<PackResponseDTO> findAll(){
        return packMapper.toResponseList(
                packRepository.findAll()
        );
    }

    public PackResponseDTO findById(Long id){
        return packRepository.findById(id)
                .map(packMapper::toResponse)
                .orElseThrow( () -> new ResourceNotFoundException("Ce pack n'existe pas"));
    }

    public List<PackResponseDTO> findByNameOrDescription(String label){
        return packMapper.toResponseList(
                packRepository.findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(label , label)
        );
    }

    @Transactional
    public PackResponseDTO create(PackRequestDTO request){

        if (packRepository.existsByNameIgnoreCase(request.name())){
            throw new DuplicateResourceException("Ce pack existe déjà");
        }

        Pack savedPack = packRepository.save(packMapper.toEntity(request));

        FleetLog log = new FleetLog();
        log.setEvent("[ Enregistrement d'une catégorie ] Nom : " + savedPack.getName());
        fleetLogRepository.save(log);

        return packMapper.toResponse(savedPack);
    }

    @Transactional
    public void addAsset(Long packId, Long assetId) {
        if (!packRepository.existsById(packId)) {
            throw new ResourceNotFoundException("Ce pack n'existe pas");
        }
        if (!assetRepository.existsById(assetId)) {
            throw new ResourceNotFoundException("Cet asset n'existe pas");
        }
        if (packRepository.existsByIdAndAssetsId(packId, assetId)) {
            throw new InvalidRequestParamException("Cet asset est déjà dans ce pack");
        }
        packRepository.addAssetToPack(packId, assetId);
    }

    @Transactional
    public void removeAsset(Long packId, Long assetId) {
        if (!packRepository.existsById(packId)) {
            throw new ResourceNotFoundException("Ce pack n'existe pas");
        }
        if (!assetRepository.existsById(assetId)) {
            throw new ResourceNotFoundException("Cet asset n'existe pas");
        }
        if (!packRepository.existsByIdAndAssetsId(packId, assetId)) {
            throw new InvalidRequestParamException("Cet asset n'est pas dans ce pack");
        }
        packRepository.removeAssetFromPack(packId, assetId);
    }

    @Transactional
    public PackResponseDTO update(Long id, PackRequestDTO request){
        Pack pack = packRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ce pack n'existe pas"));

        if (packRepository.existsByNameIgnoreCase(request.name()) && !pack.getName().equalsIgnoreCase(request.name())){
            throw new DuplicateResourceException("Ce pack existe déjà");
        }

        pack.setName(request.name());
        pack.setDescription(request.description());
        return packMapper.toResponse(pack);
    }

    @Transactional
    public void delete(Long id){
        Pack pack = packRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ce pack n'existe pas"));

        if (pack.getStatus() == PackStatus.ASSIGNED) {
            throw new InvalidRequestParamException("Ce pack est actuellement assigné");
        }

        FleetLog log = new FleetLog();
        log.setEvent("[ Suppression d'un pack ] Nom : " + pack.getName());
        fleetLogRepository.save(log);

        packRepository.delete(pack);
    }

    public long count() {
        return packRepository.count();
    }

}