package com.fleet.fleet_api.repositories;

import com.fleet.fleet_api.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String name);

    List<Department> findByNameContainsIgnoreCase(String name);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.department.id = :id")
    boolean hasUsers(@Param("id") Long id);

    boolean existsByNameIgnoreCase(String name);

}