package com.fleet.fleet_api.configs;

import com.fleet.fleet_api.models.Department;
import com.fleet.fleet_api.models.FleetLog;
import com.fleet.fleet_api.models.User;
import com.fleet.fleet_api.repositories.DepartmentRepository;
import com.fleet.fleet_api.repositories.FleetLogRepository;
import com.fleet.fleet_api.repositories.UserRepository;
import com.fleet.fleet_api.utilities.UserRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FleetLogRepository fleetLogRepository;
    private final DepartmentRepository departmentRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {

            String adminEmail = System.getenv("INITIAL_ADMIN_EMAIL");
            String adminPassword = System.getenv("INITIAL_ADMIN_PASSWORD");

            if (adminEmail == null || adminEmail.isBlank()) {
                adminEmail = "admin@fleet.com";
            }
            if (adminPassword == null || adminPassword.isBlank()) {
                adminPassword = "password123";
            }

            Department adminDepartment = departmentRepository.findByName("Direction")
                    .orElseGet(() -> {
                        Department dept = new Department();
                        dept.setName("Direction");
                        return departmentRepository.save(dept);
                    });

            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setFirstNames("Super");
            admin.setLastName("Admin");
            admin.setRole(UserRoles.ADMIN);
            admin.setDepartment(adminDepartment);

            userRepository.save(admin);

            FleetLog log = new FleetLog();
            log.setEvent("[ Création d'un Admin initial pour Fleet ] Admin créé avec succès");
            fleetLogRepository.save(log);

        }
    }
}