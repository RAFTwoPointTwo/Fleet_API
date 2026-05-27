package com.fleet.fleet_api.services;

import com.fleet.fleet_api.dtos.categoryDTO.CategoryRequestDTO;
import com.fleet.fleet_api.dtos.categoryDTO.CategoryResponseDTO;
import com.fleet.fleet_api.exceptions.DuplicateResourceException;
import com.fleet.fleet_api.exceptions.InvalidRequestParamException;
import com.fleet.fleet_api.exceptions.ResourceNotFoundException;
import com.fleet.fleet_api.mappers.CategoryMapper;
import com.fleet.fleet_api.models.Category;
import com.fleet.fleet_api.models.FleetLog;
import com.fleet.fleet_api.repositories.CategoryRepository;
import com.fleet.fleet_api.repositories.FleetLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final FleetLogRepository fleetLogRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponseDTO> findAll(){
        return categoryMapper.toResponseList(
                categoryRepository.findAll()
        );
    }

    public CategoryResponseDTO findById(Long id){
        return categoryRepository.findById(id)
                .map(categoryMapper::toResponse)
                .orElseThrow( () -> new ResourceNotFoundException("Cette catégorie n'existe pas"));
    }

    public List<CategoryResponseDTO> findByName(String name){
        return categoryMapper.toResponseList(
                categoryRepository.findByNameContainsIgnoreCase(name)
        );
    }

    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO request){

        if (categoryRepository.existsByNameIgnoreCase(request.name())){
            throw new DuplicateResourceException("Cette catégorie existe déjà");
        }

        Category savedCategory = categoryRepository.save(categoryMapper.toEntity(request));

        FleetLog log = new FleetLog();
        log.setEvent("[ Enregistrement d'une catégorie ] Nom : " + savedCategory.getName());
        fleetLogRepository.save(log);

        return categoryMapper.toResponse(savedCategory);
    }

    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO request){

        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Cette catégorie n'existe pas"));

        if (categoryRepository.existsByNameIgnoreCase(request.name()) && !category.getName().equalsIgnoreCase(request.name())){
            throw new DuplicateResourceException("Cette catégorie existe déjà");
        }

        category.setName(request.name());

        return categoryMapper.toResponse(category);
    }

    @Transactional
    public void delete(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cette catégorie n'existe pas"));

        if (categoryRepository.hasAssets(id)) {
            throw new InvalidRequestParamException("Cette catégorie contient encore des assets");
        }

        FleetLog log = new FleetLog();
        log.setEvent("[ Suppression d'une catégorie ] Nom : " + category.getName());
        fleetLogRepository.save(log);

        categoryRepository.delete(category);
    }

    public long count() {
        return categoryRepository.count();
    }

}