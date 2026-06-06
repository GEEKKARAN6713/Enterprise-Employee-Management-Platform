package com.enterprise.empmgmt.service;

import com.enterprise.empmgmt.domain.entity.Department;
import com.enterprise.empmgmt.domain.entity.EmployeeProfile;
import com.enterprise.empmgmt.domain.entity.Role;
import com.enterprise.empmgmt.domain.entity.User;
import com.enterprise.empmgmt.domain.enums.AuditAction;
import com.enterprise.empmgmt.domain.enums.RoleName;
import com.enterprise.empmgmt.dto.request.EmployeeRequest;
import com.enterprise.empmgmt.dto.response.EmployeeResponse;
import com.enterprise.empmgmt.dto.response.PageResponse;
import com.enterprise.empmgmt.exception.BusinessException;
import com.enterprise.empmgmt.exception.ResourceNotFoundException;
import com.enterprise.empmgmt.mapper.EmployeeMapper;
import com.enterprise.empmgmt.repository.DepartmentRepository;
import com.enterprise.empmgmt.repository.EmployeeProfileRepository;
import com.enterprise.empmgmt.repository.RoleRepository;
import com.enterprise.empmgmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeProfileRepository employeeProfileRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }
        Role role = roleRepository.findByName(RoleName.EMPLOYEE)
                .orElseThrow(() -> new BusinessException("EMPLOYEE role not configured"));

        User user = User.builder()
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode("ChangeMe1!"))
                .enabled(true)
                .roles(Set.of(role))
                .build();
        user = userRepository.save(user);

        EmployeeProfile profile = mapToProfile(new EmployeeProfile(), request);
        profile.setUser(user);
        profile = employeeProfileRepository.save(profile);
        auditService.log(AuditAction.CREATE, "EmployeeProfile", profile.getId(), "Employee created");
        return employeeMapper.toResponse(profile);
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getById(Long id) {
        return employeeMapper.toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<EmployeeResponse> list(String search, Long departmentId, Pageable pageable) {
        Page<EmployeeProfile> page;
        if (departmentId != null) {
            page = employeeProfileRepository.findByDepartmentId(departmentId, pageable);
        } else if (search != null && !search.isBlank()) {
            page = employeeProfileRepository.search(search, pageable);
        } else {
            page = employeeProfileRepository.findAll(pageable);
        }
        return PageResponse.from(page.map(employeeMapper::toResponse));
    }

    @Transactional
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        EmployeeProfile profile = findById(id);
        profile = mapToProfile(profile, request);
        profile = employeeProfileRepository.save(profile);
        auditService.log(AuditAction.UPDATE, "EmployeeProfile", id, "Employee updated");
        return employeeMapper.toResponse(profile);
    }

    @Transactional
    public void delete(Long id) {
        EmployeeProfile profile = findById(id);
        userRepository.delete(profile.getUser());
        auditService.log(AuditAction.DELETE, "EmployeeProfile", id, "Employee deleted");
    }

    private EmployeeProfile mapToProfile(EmployeeProfile profile, EmployeeRequest request) {
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setPhone(request.getPhone());
        profile.setJobTitle(request.getJobTitle());
        profile.setHireDate(request.getHireDate());
        profile.setSalary(request.getSalary());
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            profile.setDepartment(department);
        }
        if (request.getManagerId() != null) {
            EmployeeProfile manager = findById(request.getManagerId());
            profile.setManager(manager);
        }
        return profile;
    }

    private EmployeeProfile findById(Long id) {
        return employeeProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
    }
}
