package com.enterprise.empmgmt.service;

import com.enterprise.empmgmt.domain.entity.Department;
import com.enterprise.empmgmt.domain.enums.AuditAction;
import com.enterprise.empmgmt.dto.request.DepartmentRequest;
import com.enterprise.empmgmt.dto.response.DepartmentResponse;
import com.enterprise.empmgmt.dto.response.PageResponse;
import com.enterprise.empmgmt.exception.BusinessException;
import com.enterprise.empmgmt.exception.ResourceNotFoundException;
import com.enterprise.empmgmt.mapper.DepartmentMapper;
import com.enterprise.empmgmt.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final AuditService auditService;

    @Transactional
    public DepartmentResponse create(DepartmentRequest request) {
        if (departmentRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BusinessException("Department already exists");
        }
        Department department = Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        department = departmentRepository.save(department);
        auditService.log(AuditAction.CREATE, "Department", department.getId(), "Department created");
        return departmentMapper.toResponse(department);
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getById(Long id) {
        return departmentMapper.toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<DepartmentResponse> list(String search, Pageable pageable) {
        Page<Department> page = (search == null || search.isBlank())
                ? departmentRepository.findAll(pageable)
                : departmentRepository.findByNameContainingIgnoreCase(search, pageable);
        return PageResponse.from(page.map(departmentMapper::toResponse));
    }

    @Transactional
    public DepartmentResponse update(Long id, DepartmentRequest request) {
        Department department = findById(id);
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department = departmentRepository.save(department);
        auditService.log(AuditAction.UPDATE, "Department", id, "Department updated");
        return departmentMapper.toResponse(department);
    }

    @Transactional
    public void delete(Long id) {
        Department department = findById(id);
        departmentRepository.delete(department);
        auditService.log(AuditAction.DELETE, "Department", id, "Department deleted");
    }

    private Department findById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + id));
    }
}
