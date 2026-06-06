package com.enterprise.empmgmt.mapper;

import com.enterprise.empmgmt.domain.entity.AuditLog;
import com.enterprise.empmgmt.dto.response.AuditLogResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    AuditLogResponse toResponse(AuditLog auditLog);
}
