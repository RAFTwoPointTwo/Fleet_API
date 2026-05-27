package com.fleet.fleet_api.mappers;

import com.fleet.fleet_api.dtos.categoryDTO.CategoryRequestDTO;
import com.fleet.fleet_api.dtos.categoryDTO.CategoryResponseDTO;
import com.fleet.fleet_api.models.Category;
import com.fleet.fleet_api.utilities.AssetStatus;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper implements Mapper<Category, CategoryResponseDTO, CategoryRequestDTO> {

    @Override
    public CategoryResponseDTO toResponse(Category category) {
        long total = category.getAssets().size();
        long available = category.getAssets().stream()
                .filter(a -> a.getStatus() != null && AssetStatus.AVAILABLE == a.getStatus())
                .count();

        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                total,
                available
        );
    }

    @Override
    public Category toEntity(CategoryRequestDTO request) {
        Category category = new Category();
        category.setName(request.name());
        return category;
    }
}
