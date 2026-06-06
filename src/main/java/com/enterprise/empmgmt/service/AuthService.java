package com.enterprise.empmgmt.service;

import com.enterprise.empmgmt.domain.entity.EmployeeProfile;
import com.enterprise.empmgmt.domain.entity.Role;
import com.enterprise.empmgmt.domain.entity.User;
import com.enterprise.empmgmt.domain.enums.AuditAction;
import com.enterprise.empmgmt.domain.enums.RoleName;
import com.enterprise.empmgmt.dto.request.LoginRequest;
import com.enterprise.empmgmt.dto.request.RefreshTokenRequest;
import com.enterprise.empmgmt.dto.request.RegisterRequest;
import com.enterprise.empmgmt.dto.response.AuthResponse;
import com.enterprise.empmgmt.exception.BusinessException;
import com.enterprise.empmgmt.repository.EmployeeProfileRepository;
import com.enterprise.empmgmt.repository.RoleRepository;
import com.enterprise.empmgmt.repository.UserRepository;
import com.enterprise.empmgmt.security.CustomUserDetails;
import com.enterprise.empmgmt.security.CustomUserDetailsService;
import com.enterprise.empmgmt.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final AuditService auditService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already registered");
        }
        if (request.getRole() == RoleName.ADMIN) {
            throw new BusinessException("ADMIN role cannot be self-assigned during registration");
        }
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new BusinessException("Invalid role"));

        User user = User.builder()
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .roles(Set.of(role))
                .build();
        user = userRepository.save(user);

        EmployeeProfile profile = EmployeeProfile.builder()
                .user(user)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        employeeProfileRepository.save(profile);

        auditService.log(AuditAction.CREATE, "User", user.getId(), "User registered");
        return buildAuthResponse(userDetailsService.loadUserByUsername(user.getEmail()), user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().toLowerCase(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new BusinessException("User not found"));
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        auditService.log(AuditAction.LOGIN, "User", user.getId(), "User logged in");
        return buildAuthResponse(userDetails, user);
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        User user = userRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new BusinessException("Invalid refresh token"));
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        if (!jwtTokenProvider.isTokenValid(request.getRefreshToken(), userDetails)
                || !jwtTokenProvider.isRefreshToken(request.getRefreshToken())) {
            throw new BusinessException("Invalid or expired refresh token");
        }
        String newRefresh = jwtTokenProvider.generateRefreshToken(userDetails);
        user.setRefreshToken(newRefresh);
        userRepository.save(user);
        return buildAuthResponse(userDetails, user);
    }

    private AuthResponse buildAuthResponse(UserDetails userDetails, User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        Set<String> roles = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(user.getRefreshToken())
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenExpirationMs())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }
}
