package com.fleet.fleet_api.mappers;

import com.fleet.fleet_api.dtos.userDTO.UserBaseInfoResponseDTO;
import com.fleet.fleet_api.dtos.userDTO.UserRegisterRequestDTO;
import com.fleet.fleet_api.dtos.userDTO.UserResponseDTO;
import com.fleet.fleet_api.models.User;
import com.fleet.fleet_api.models.Department;
import com.fleet.fleet_api.utilities.UserRoles;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<User, UserResponseDTO, UserRegisterRequestDTO> {

    @Override
    public UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getLastName(),
                user.getFirstNames(),
                user.getEmail(),
                user.getRole(),
                user.getDepartment() != null ? user.getDepartment().getName() : null
        );
    }

    @Override
    public User toEntity(UserRegisterRequestDTO request) {
        User user = new User();
        user.setLastName(request.lastName());
        user.setFirstNames(request.firstNames());
        user.setEmail(request.email());
        user.setRole(UserRoles.STANDARD_USER);
        return user;
    }

    public User toEntity(UserRegisterRequestDTO request, Department department) {
        User user = this.toEntity(request);
        user.setDepartment(department);
        return user;
    }

    public UserBaseInfoResponseDTO toBaseInfo(User user) {
        return new UserBaseInfoResponseDTO(
                user.getFullName(),
                user.getDepartment().getName()
        );
    }
}