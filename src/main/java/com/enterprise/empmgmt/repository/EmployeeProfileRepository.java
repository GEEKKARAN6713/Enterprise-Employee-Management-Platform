package com.enterprise.empmgmt.repository;

import com.enterprise.empmgmt.domain.entity.EmployeeProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {

    Optional<EmployeeProfile> findByUserId(Long userId);

    Optional<EmployeeProfile> findByUserEmail(String email);

    @Query("""
            SELECT e FROM EmployeeProfile e
            WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(e.jobTitle) LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    Page<EmployeeProfile> search(@Param("search") String search, Pageable pageable);

    Page<EmployeeProfile> findByDepartmentId(Long departmentId, Pageable pageable);
}
