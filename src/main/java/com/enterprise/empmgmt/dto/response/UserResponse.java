package com.enterprise.empmgmt.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User summary")
public class UserResponse {

    private Long id;
    private String email;
    private boolean enabled;
    private Set<String> roles;
    private Instant createdAt;
}
