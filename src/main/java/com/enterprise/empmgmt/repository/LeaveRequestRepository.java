package com.enterprise.empmgmt.repository;

import com.enterprise.empmgmt.domain.entity.LeaveRequest;
import com.enterprise.empmgmt.domain.enums.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    Page<LeaveRequest> findByEmployeeId(Long employeeId, Pageable pageable);

    Page<LeaveRequest> findByStatus(LeaveStatus status, Pageable pageable);

    Page<LeaveRequest> findByEmployeeDepartmentId(Long departmentId, Pageable pageable);
}
