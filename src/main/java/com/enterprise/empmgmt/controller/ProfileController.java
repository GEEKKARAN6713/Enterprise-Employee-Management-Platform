package com.enterprise.empmgmt.controller;

import com.enterprise.empmgmt.dto.request.ProfileUpdateRequest;
import com.enterprise.empmgmt.dto.response.EmployeeResponse;
import com.enterprise.empmgmt.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "Current user profile management")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    @Operation(summary = "Get current user profile")
    public EmployeeResponse getProfile() {
        return profileService.getCurrentProfile();
    }

    @PutMapping
    @Operation(summary = "Update current user profile")
    public EmployeeResponse updateProfile(@Valid @RequestBody ProfileUpdateRequest request) {
        return profileService.updateCurrentProfile(request);
    }
}
