package com.fleet.fleet_api.services;

import com.fleet.fleet_api.dtos.userDTO.*;
import com.fleet.fleet_api.exceptions.AuthException;
import com.fleet.fleet_api.exceptions.InvalidRequestParamException;
import com.fleet.fleet_api.exceptions.ResourceNotFoundException;
import com.fleet.fleet_api.mappers.UserMapper;
import com.fleet.fleet_api.models.Department;
import com.fleet.fleet_api.models.FleetLog;
import com.fleet.fleet_api.models.User;
import com.fleet.fleet_api.repositories.DepartmentRepository;
import com.fleet.fleet_api.repositories.FleetLogRepository;
import com.fleet.fleet_api.repositories.UserRepository;
import com.fleet.fleet_api.utilities.UserRoles;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final FleetLogRepository fleetLogRepository;
    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    public List<UserResponseDTO> findALl(){
        return userMapper.toResponseList(
                userRepository.findAll()
        );
    }

    public UserResponseDTO findById(Long id){
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
    }

    public List<UserResponseDTO> findByName(String name){
        return userMapper.toResponseList(
                userRepository.findByLastNameContainsIgnoreCaseOrFirstNamesContainsIgnoreCase(name , name)
        );
    }

    public List<UserResponseDTO> findByDepartmentId(Long departmentId){
        return userMapper.toResponseList(
                userRepository.findByDepartmentId(departmentId)
        );
    }

    public List<UserResponseDTO> findByRole(UserRoles role){
        return userMapper.toResponseList(
                userRepository.findByRole(role)
        );
    }

    @Transactional
    public UserResponseDTO register(UserRegisterRequestDTO request){
        if (userRepository.existsByEmail(request.email())){
            throw new InvalidRequestParamException("Cet email existe déjà");
        }

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Le département mentionné est introuvable"));

        User user = userMapper.toEntity(request , department);

        user.setPassword(passwordEncoder.encode(request.password()));

        User savedUser = userRepository.save(user);

        FleetLog log = new FleetLog();
        log.setEvent("[ Enregistrement d'un Utilisateur ] Utilisateur " + savedUser.getFullName() + " enregistré");
        fleetLogRepository.save(log);

        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public UserResponseDTO login(UserLoginRequestDTO request , HttpServletRequest servletRequest){
        Optional<User> userOpt = userRepository.findByEmail(request.email());

        if (userOpt.isEmpty()){
            passwordEncoder.encode(request.password());
            throw new AuthException("Identifiants invalides");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(request.password() , user.getPassword())){
            throw new AuthException("Identifiants invalides");
        }

        HttpSession session = servletRequest.getSession(true);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                Collections.singletonList(() -> user.getRole().name())
        );

        SecurityContext securityContext = SecurityContextHolder.getContext();

        securityContext.setAuthentication(authentication);

        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

        return userMapper.toResponse(user);
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @Transactional
    public UserResponseDTO updateProfile(Long id , UserUpdateProfileDTO request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        user.setLastName(request.lastName());
        user.setFirstNames(request.firstNames());

        if (!user.getDepartment().getId().equals(request.departmentId())) {
            Department department = departmentRepository.findById(request.departmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Département indiqué introuvable"));
            user.setDepartment(department);
        }

        return userMapper.toResponse(user);
    }

    @Transactional
    public void updateRole(Long id , UserUpdateRoleDTO request){
        User admin = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin introuvable"));

        if (admin.getRole() != UserRoles.ADMIN){
            throw new InvalidRequestParamException("Modification refusée ! Seuls les admins peuvent modifier les roles utilisateurs");
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        UserRoles lastRole = user.getRole();

        user.setRole(request.newRole());

        FleetLog log = new FleetLog();
        log.setEvent("[ Modification de Role Utilisateur ] Utilisateur " + user.getFullName() + " ( Ancien rôle : " + lastRole + " ) devenu < " + user.getRole() + " >. Modifié par " + admin.getFullName());
        fleetLogRepository.save(log);
    }

    public long count(){
        return userRepository.count();
    }

}