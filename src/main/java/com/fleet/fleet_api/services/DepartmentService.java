package com.fleet.fleet_api.services;

import com.fleet.fleet_api.dtos.departmentDTO.DepartmentRequestDTO;
import com.fleet.fleet_api.dtos.departmentDTO.DepartmentResponseDTO;
import com.fleet.fleet_api.exceptions.DuplicateResourceException;
import com.fleet.fleet_api.exceptions.InvalidRequestParamException;
import com.fleet.fleet_api.exceptions.ResourceNotFoundException;
import com.fleet.fleet_api.mappers.DepartmentMapper;
import com.fleet.fleet_api.models.Department;
import com.fleet.fleet_api.models.FleetLog;
import com.fleet.fleet_api.repositories.DepartmentRepository;
import com.fleet.fleet_api.repositories.FleetLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FleetLogRepository fleetLogRepository;
    private final DepartmentMapper departmentMapper;

    public List<DepartmentResponseDTO> findAll(){
        return departmentMapper.toResponseList(
                departmentRepository.findAll()
        );
    }

    public DepartmentResponseDTO findById(Long id){
        return departmentRepository.findById(id)
                .map(departmentMapper::toResponse)
                .orElseThrow( () -> new ResourceNotFoundException("Ce département n'existe pas"));
    }

    public List<DepartmentResponseDTO> findByName(String name){
        return departmentMapper.toResponseList(
                departmentRepository.findByNameContainsIgnoreCase(name)
        );
    }

    @Transactional
    public DepartmentResponseDTO create(DepartmentRequestDTO request){

        if (departmentRepository.existsByNameIgnoreCase(request.name())){
            throw new DuplicateResourceException("Ce département existe déjà");
        }

        Department savedDepartment = departmentRepository.save(departmentMapper.toEntity(request));

        FleetLog log = new FleetLog();
        log.setEvent("[ Enregistrement d'un département ] Nom : " + savedDepartment.getName());
        fleetLogRepository.save(log);

        return departmentMapper.toResponse(savedDepartment);
    }

    @Transactional
    public DepartmentResponseDTO update(Long id, DepartmentRequestDTO request){
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ce département n'existe pas"));

        if (departmentRepository.existsByNameIgnoreCase(request.name()) && !department.getName().equalsIgnoreCase(request.name())){
            throw new DuplicateResourceException("Ce département existe déjà");
        }

        department.setName(request.name());

        return departmentMapper.toResponse(department);
    }

    @Transactional
    public void delete(Long id){
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ce département n'existe pas"));

        if (departmentRepository.hasUsers(id)) {
            throw new InvalidRequestParamException("Ce département possède encore des utilisateurs");
        }

        FleetLog log = new FleetLog();
        log.setEvent("[ Suppression d'un département ] Nom : " + department.getName());
        fleetLogRepository.save(log);

        departmentRepository.delete(department);
    }

    public long count() {
        return departmentRepository.count();
    }

}
