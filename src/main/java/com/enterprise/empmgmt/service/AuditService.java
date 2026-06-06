package com.enterprise.empmgmt.service;

import com.enterprise.empmgmt.domain.entity.AuditLog;
import com.enterprise.empmgmt.domain.enums.AuditAction;
import com.enterprise.empmgmt.dto.response.AuditLogResponse;
import com.enterprise.empmgmt.dto.response.PageResponse;
import com.enterprise.empmgmt.mapper.AuditLogMapper;
import com.enterprise.empmgmt.repository.AuditLogRepository;
import com.enterprise.empmgmt.security.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Transactional
    public void log(AuditAction action, String entityType, Long entityId, String details) {
        AuditLog log = AuditLog.builder()
                .actorEmail(SecurityUtils.currentUserEmail())
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .details(details)
                .ipAddress(resolveClientIp())
                .build();
        auditLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public PageResponse<AuditLogResponse> findAll(String search, Pageable pageable) {
        Page<AuditLog> page = (search == null || search.isBlank())
                ? auditLogRepository.findAll(pageable)
                : auditLogRepository.findByActorEmailContainingIgnoreCase(search, pageable);
        return PageResponse.from(page.map(auditLogMapper::toResponse));
    }

    private String resolveClientIp() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
