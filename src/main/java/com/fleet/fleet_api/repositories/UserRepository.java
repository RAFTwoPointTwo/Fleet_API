package com.fleet.fleet_api.repositories;

import com.fleet.fleet_api.models.User;
import com.fleet.fleet_api.utilities.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User , Long> {

    Optional<User> findByEmail(String email);

    List<User> findByLastNameContainsIgnoreCaseOrFirstNamesContainsIgnoreCase(String lastName , String firstNames);

    List<User> findByDepartmentId(Long departmentId);

    List<User> findByRole(UserRoles role);

    boolean existsByEmail(String email);

}