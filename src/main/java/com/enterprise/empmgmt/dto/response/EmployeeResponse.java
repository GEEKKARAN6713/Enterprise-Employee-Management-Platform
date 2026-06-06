package com.enterprise.empmgmt.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Employee profile response")
public class EmployeeResponse {

    private Long id;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String jobTitle;
    private LocalDate hireDate;
    private BigDecimal salary;
    private Long departmentId;
    private String departmentName;
    private Long managerId;
    private String managerName;
    private Instant createdAt;
}
