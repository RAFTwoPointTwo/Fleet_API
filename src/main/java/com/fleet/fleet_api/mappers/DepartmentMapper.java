package com.fleet.fleet_api.mappers;

import com.fleet.fleet_api.dtos.departmentDTO.DepartmentRequestDTO;
import com.fleet.fleet_api.dtos.departmentDTO.DepartmentResponseDTO;
import com.fleet.fleet_api.models.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper implements Mapper<Department, DepartmentResponseDTO, DepartmentRequestDTO> {

    @Override
    public DepartmentResponseDTO toResponse(Department department) {
        return new DepartmentResponseDTO(
                department.getId(),
                department.getName()
        );
    }

    @Override
    public Department toEntity(DepartmentRequestDTO request) {
        Department department = new Department();
        department.setName(request.name());
        return department;
    }
}
