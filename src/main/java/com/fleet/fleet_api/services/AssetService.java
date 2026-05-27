package com.fleet.fleet_api.services;

import com.fleet.fleet_api.dtos.assetDTO.AssetRequestDTO;
import com.fleet.fleet_api.dtos.assetDTO.AssetResponseDTO;
import com.fleet.fleet_api.exceptions.DuplicateResourceException;
import com.fleet.fleet_api.exceptions.InvalidAssetStateException;
import com.fleet.fleet_api.exceptions.ResourceNotFoundException;
import com.fleet.fleet_api.mappers.AssetMapper;
import com.fleet.fleet_api.models.Asset;
import com.fleet.fleet_api.models.Category;
import com.fleet.fleet_api.models.FleetLog;
import com.fleet.fleet_api.repositories.AssetRepository;
import com.fleet.fleet_api.repositories.CategoryRepository;
import com.fleet.fleet_api.repositories.FleetLogRepository;
import com.fleet.fleet_api.utilities.AssetStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssetService {

    private final AssetRepository assetRepository;
    private final CategoryRepository categoryRepository;
    private final FleetLogRepository fleetLogRepository;
    private final AssetMapper assetMapper;

    public List<AssetResponseDTO> findAll(){
        return assetMapper.toResponseList(
                assetRepository.findAll()
        );
    }

    public AssetResponseDTO findById(Long id){
        return assetRepository.findById(id)
                .map(assetMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Cet asset n'existe pas"));
    }

    public AssetResponseDTO findBySerialNumber(String serialNumber){
        return assetRepository.findBySerialNumber(serialNumber)
                .map(assetMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun asset trouvé"));
    }

    public List<AssetResponseDTO> findByName(String name){
        return assetMapper.toResponseList(
                assetRepository.findByNameContainsIgnoreCase(name)
        );
    }

    public List<AssetResponseDTO> findByStatus(AssetStatus status){
        return assetMapper.toResponseList(
                assetRepository.findByStatus(status)
        );
    }

    public List<AssetResponseDTO> findByCategoryId(Long categoryId){
        return assetMapper.toResponseList(
                assetRepository.findByCategoryId(categoryId)
        );
    }

    public List<AssetResponseDTO> findByPackId(Long packId){
        return assetMapper.toResponseList(
                assetRepository.findByPacksId(packId)
        );
    }

    public List<AssetResponseDTO> findAvailable(){
        return assetMapper.toResponseList(
                assetRepository.findByStatus(AssetStatus.AVAILABLE)
        );
    }

    @Transactional
    public AssetResponseDTO create(AssetRequestDTO request){
        if (assetRepository.existsBySerialNumber(request.serialNumber())){
            throw new DuplicateResourceException("Ce numéro de série est déjà utilisé");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("La catégorie sélectionnée n'existe pas"));

        Asset savedAsset = assetRepository.save(
                assetMapper.toEntity(request , category)
        );

        FleetLog log = new FleetLog();
        log.setEvent("[ Enregistrement d'asset ] Nom : " + savedAsset.getName());
        fleetLogRepository.save(log);

        return assetMapper.toResponse(savedAsset);
    }

    @Transactional
    public AssetResponseDTO update(Long id , AssetRequestDTO request){
        Asset asset = assetRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cet asset n'existe pas")
        );

        asset.setName(request.name());

        if (!asset.getCategory().getId().equals(request.categoryId())){
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("La catégorie sélectionnée n'existe pas"));
            asset.setCategory(category);
        }

        return assetMapper.toResponse(asset);
    }

    @Transactional
    public void delete(Long id){
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cet asset n'existe pas"));

        if (asset.getStatus() == AssetStatus.ASSIGNED) {
            throw new InvalidAssetStateException("Cet asset est actuellement assigné");
        }

        FleetLog log = new FleetLog();
        log.setEvent("Suppression d'asset -> ID : " + asset.getId());
        fleetLogRepository.save(log);

        assetRepository.delete(asset);

    }

    public long count() {
        return assetRepository.count();
    }

    public long countByCategoryId(Long categoryId){
        return assetRepository.countByCategoryId(categoryId);
    }

}