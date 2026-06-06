package com.enterprise.empmgmt.config;

import com.enterprise.empmgmt.domain.entity.Department;
import com.enterprise.empmgmt.domain.entity.EmployeeProfile;
import com.enterprise.empmgmt.domain.entity.Role;
import com.enterprise.empmgmt.domain.entity.User;
import com.enterprise.empmgmt.domain.enums.RoleName;
import com.enterprise.empmgmt.repository.DepartmentRepository;
import com.enterprise.empmgmt.repository.EmployeeProfileRepository;
import com.enterprise.empmgmt.repository.RoleRepository;
import com.enterprise.empmgmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminBootstrap implements ApplicationRunner {

    public static final String ADMIN_EMAIL = "admin@enterprise.com";
    public static final String ADMIN_DEFAULT_PASSWORD = "Admin123!";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.existsByEmail(ADMIN_EMAIL)) {
            return;
        }
        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseThrow(() -> new IllegalStateException("ADMIN role missing — check Flyway migrations"));

        User admin = User.builder()
                .email(ADMIN_EMAIL)
                .password(passwordEncoder.encode(ADMIN_DEFAULT_PASSWORD))
                .enabled(true)
                .roles(Set.of(adminRole))
                .build();
        admin = userRepository.save(admin);

        Department hq = departmentRepository.findAll().stream().findFirst()
                .orElseGet(() -> departmentRepository.save(Department.builder()
                        .name("Headquarters")
                        .description("Default corporate department")
                        .build()));

        EmployeeProfile profile = EmployeeProfile.builder()
                .user(admin)
                .firstName("System")
                .lastName("Administrator")
                .jobTitle("Platform Admin")
                .hireDate(LocalDate.now())
                .department(hq)
                .build();
        employeeProfileRepository.save(profile);
        log.info("Default admin user created: {} / {}", ADMIN_EMAIL, ADMIN_DEFAULT_PASSWORD);
    }
}
