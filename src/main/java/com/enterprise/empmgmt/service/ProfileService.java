package com.enterprise.empmgmt.service;

import com.enterprise.empmgmt.domain.entity.EmployeeProfile;
import com.enterprise.empmgmt.domain.enums.AuditAction;
import com.enterprise.empmgmt.dto.request.ProfileUpdateRequest;
import com.enterprise.empmgmt.dto.response.EmployeeResponse;
import com.enterprise.empmgmt.exception.ResourceNotFoundException;
import com.enterprise.empmgmt.mapper.EmployeeMapper;
import com.enterprise.empmgmt.repository.EmployeeProfileRepository;
import com.enterprise.empmgmt.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final EmployeeProfileRepository employeeProfileRepository;
    private final EmployeeMapper employeeMapper;
    private final AuditService auditService;

    @Transactional(readOnly = true)
    public EmployeeResponse getCurrentProfile() {
        return employeeMapper.toResponse(findCurrentProfile());
    }

    @Transactional
    public EmployeeResponse updateCurrentProfile(ProfileUpdateRequest request) {
        EmployeeProfile profile = findCurrentProfile();
        if (request.getFirstName() != null) {
            profile.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            profile.setLastName(request.getLastName());
        }
        if (request.getPhone() != null) {
            profile.setPhone(request.getPhone());
        }
        if (request.getJobTitle() != null) {
            profile.setJobTitle(request.getJobTitle());
        }
        profile = employeeProfileRepository.save(profile);
        auditService.log(AuditAction.UPDATE, "EmployeeProfile", profile.getId(), "Profile updated");
        return employeeMapper.toResponse(profile);
    }

    private EmployeeProfile findCurrentProfile() {
        String email = SecurityUtils.currentUserEmail();
        return employeeProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found"));
    }
}
