package com.enterprise.empmgmt.service;

import com.enterprise.empmgmt.domain.entity.Role;
import com.enterprise.empmgmt.domain.entity.User;
import com.enterprise.empmgmt.domain.enums.RoleName;
import com.enterprise.empmgmt.dto.request.LoginRequest;
import com.enterprise.empmgmt.dto.request.RegisterRequest;
import com.enterprise.empmgmt.dto.response.AuthResponse;
import com.enterprise.empmgmt.exception.BusinessException;
import com.enterprise.empmgmt.repository.EmployeeProfileRepository;
import com.enterprise.empmgmt.repository.RoleRepository;
import com.enterprise.empmgmt.repository.UserRepository;
import com.enterprise.empmgmt.security.CustomUserDetailsService;
import com.enterprise.empmgmt.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private EmployeeProfileRepository employeeProfileRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private CustomUserDetailsService userDetailsService;
    @Mock private AuditService auditService;

    @InjectMocks private AuthService authService;

    @Test
    void register_shouldRejectDuplicateEmail() {
        RegisterRequest request = RegisterRequest.builder()
                .email("user@test.com")
                .password("Secure1!")
                .firstName("Jane")
                .lastName("Doe")
                .role(RoleName.EMPLOYEE)
                .build();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already registered");
    }

    @Test
    void login_shouldReturnTokens() {
        LoginRequest request = LoginRequest.builder()
                .email("user@test.com")
                .password("Secure1!")
                .build();
        User user = User.builder()
        .email("user@test.com")
        .roles(Set.of())
        .build();
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("user@test.com").password("x").roles("EMPLOYEE").build();

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(userDetailsService.loadUserByUsername("user@test.com")).thenReturn(userDetails);
        when(jwtTokenProvider.generateAccessToken(userDetails)).thenReturn("access");
        when(jwtTokenProvider.generateRefreshToken(userDetails)).thenReturn("refresh");
        when(jwtTokenProvider.getAccessTokenExpirationMs()).thenReturn(900000L);

        AuthResponse response = authService.login(request);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertThat(response.getAccessToken()).isEqualTo("access");
        assertThat(response.getRefreshToken()).isEqualTo("refresh");
    }
}
