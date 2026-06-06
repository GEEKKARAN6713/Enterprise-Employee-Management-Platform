package com.enterprise.empmgmt.service;

import com.enterprise.empmgmt.domain.entity.EmployeeProfile;
import com.enterprise.empmgmt.domain.entity.LeaveRequest;
import com.enterprise.empmgmt.domain.enums.AuditAction;
import com.enterprise.empmgmt.domain.enums.LeaveStatus;
import com.enterprise.empmgmt.dto.request.LeaveRequestDto;
import com.enterprise.empmgmt.dto.request.LeaveReviewRequest;
import com.enterprise.empmgmt.dto.response.LeaveResponse;
import com.enterprise.empmgmt.dto.response.PageResponse;
import com.enterprise.empmgmt.exception.BusinessException;
import com.enterprise.empmgmt.exception.ResourceNotFoundException;
import com.enterprise.empmgmt.mapper.LeaveMapper;
import com.enterprise.empmgmt.repository.EmployeeProfileRepository;
import com.enterprise.empmgmt.repository.LeaveRequestRepository;
import com.enterprise.empmgmt.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final LeaveMapper leaveMapper;
    private final AuditService auditService;

    @Transactional
    public LeaveResponse submit(LeaveRequestDto request) {
        EmployeeProfile employee = currentEmployee();
        LeaveRequest leave = LeaveRequest.builder()
                .employee(employee)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .status(LeaveStatus.PENDING)
                .build();
        leave = leaveRequestRepository.save(leave);
        auditService.log(AuditAction.CREATE, "LeaveRequest", leave.getId(), "Leave submitted");
        return leaveMapper.toResponse(leave);
    }

    @Transactional
    public LeaveResponse review(Long id, LeaveReviewRequest request) {
        LeaveRequest leave = findById(id);
        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new BusinessException("Only pending leave requests can be reviewed");
        }
        if (request.getStatus() != LeaveStatus.APPROVED && request.getStatus() != LeaveStatus.REJECTED) {
            throw new BusinessException("Review status must be APPROVED or REJECTED");
        }
        leave.setStatus(request.getStatus());
        leave.setReviewComment(request.getReviewComment());
        leave.setReviewedBy(currentEmployee());
        leave = leaveRequestRepository.save(leave);
        AuditAction action = request.getStatus() == LeaveStatus.APPROVED ? AuditAction.APPROVE : AuditAction.REJECT;
        auditService.log(action, "LeaveRequest", id, "Leave " + request.getStatus().name().toLowerCase());
        return leaveMapper.toResponse(leave);
    }

    @Transactional
    public LeaveResponse cancel(Long id) {
        LeaveRequest leave = findById(id);
        EmployeeProfile current = currentEmployee();
        if (!leave.getEmployee().getId().equals(current.getId())) {
            throw new BusinessException("You can only cancel your own leave requests");
        }
        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new BusinessException("Only pending requests can be cancelled");
        }
        leave.setStatus(LeaveStatus.CANCELLED);
        leave = leaveRequestRepository.save(leave);
        auditService.log(AuditAction.UPDATE, "LeaveRequest", id, "Leave cancelled");
        return leaveMapper.toResponse(leave);
    }

    @Transactional(readOnly = true)
    public LeaveResponse getById(Long id) {
        return leaveMapper.toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<LeaveResponse> list(LeaveStatus status, Long employeeId, Pageable pageable) {
        Page<LeaveRequest> page;
        if (employeeId != null) {
            page = leaveRequestRepository.findByEmployeeId(employeeId, pageable);
        } else if (status != null) {
            page = leaveRequestRepository.findByStatus(status, pageable);
        } else {
            page = leaveRequestRepository.findAll(pageable);
        }
        return PageResponse.from(page.map(leaveMapper::toResponse));
    }

    private LeaveRequest findById(Long id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found: " + id));
    }

    private EmployeeProfile currentEmployee() {
        return employeeProfileRepository.findByUserEmail(SecurityUtils.currentUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found"));
    }
}
