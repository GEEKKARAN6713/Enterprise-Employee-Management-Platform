package com.enterprise.empmgmt.service;

import com.enterprise.empmgmt.domain.entity.Department;
import com.enterprise.empmgmt.dto.request.DepartmentRequest;
import com.enterprise.empmgmt.dto.response.DepartmentResponse;
import com.enterprise.empmgmt.exception.BusinessException;
import com.enterprise.empmgmt.mapper.DepartmentMapper;
import com.enterprise.empmgmt.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock private DepartmentRepository departmentRepository;
    @Mock private DepartmentMapper departmentMapper;
    @Mock private AuditService auditService;

    @InjectMocks private DepartmentService departmentService;

    @Test
    void create_shouldRejectDuplicateName() {
        DepartmentRequest request = DepartmentRequest.builder().name("Engineering").build();
        when(departmentRepository.existsByNameIgnoreCase("Engineering")).thenReturn(true);

        assertThatThrownBy(() -> departmentService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already exists");
    }
}
