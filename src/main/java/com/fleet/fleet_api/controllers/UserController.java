package com.fleet.fleet_api.controllers;

import com.fleet.fleet_api.dtos.userDTO.*;
import com.fleet.fleet_api.services.UserService;
import com.fleet.fleet_api.utilities.UserRoles;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return ResponseEntity.ok(userService.findALl());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> searchUser(@RequestParam String name) {
        return ResponseEntity.ok(userService.findByName(name));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getUsersByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(userService.findByDepartmentId(departmentId));
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(@PathVariable UserRoles role) {
        return ResponseEntity.ok(userService.findByRole(role));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getUsersCount() {
        return ResponseEntity.ok(userService.count());
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRegisterRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO request , HttpServletRequest servletRequest) {
        return ResponseEntity.ok(userService.login(request , servletRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/profile")
    public ResponseEntity<UserResponseDTO> updateProfile(@PathVariable Long id, @Valid @RequestBody UserUpdateProfileDTO request) {
        return ResponseEntity.ok(userService.updateProfile(id, request));
    }

    @PatchMapping("/{id}/update/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> updateUserRole(@PathVariable Long id, @Valid @RequestBody UserUpdateRoleDTO request) {
        userService.updateRole(id, request);
        return ResponseEntity.noContent().build();
    }
}