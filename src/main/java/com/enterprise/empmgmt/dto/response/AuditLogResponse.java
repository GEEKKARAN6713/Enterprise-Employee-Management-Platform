package com.enterprise.empmgmt.dto.response;

import com.enterprise.empmgmt.domain.enums.AuditAction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Audit log entry")
public class AuditLogResponse {

    private Long id;
    private String actorEmail;
    private AuditAction action;
    private String entityType;
    private Long entityId;
    private String details;
    private String ipAddress;
    private Instant createdAt;
}
