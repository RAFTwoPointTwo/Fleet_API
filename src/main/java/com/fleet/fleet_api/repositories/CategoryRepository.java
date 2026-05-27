package com.fleet.fleet_api.repositories;

import com.fleet.fleet_api.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category , Long> {

    List<Category> findByNameContainsIgnoreCase(String name);

    @Query("SELECT COUNT(a) > 0 FROM Asset a WHERE a.category.id = :id")
    boolean hasAssets(@Param("id") Long id);

    boolean existsByNameIgnoreCase(String name);

}