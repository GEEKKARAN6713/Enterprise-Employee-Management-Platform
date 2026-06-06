package com.enterprise.empmgmt.mapper;

import com.enterprise.empmgmt.domain.entity.LeaveRequest;
import com.enterprise.empmgmt.dto.response.LeaveResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface LeaveMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeName", source = "employee", qualifiedByName = "employeeName")
    @Mapping(target = "reviewedByName", source = "reviewedBy", qualifiedByName = "employeeName")
    LeaveResponse toResponse(LeaveRequest leave);

    @Named("employeeName")
    default String employeeName(com.enterprise.empmgmt.domain.entity.EmployeeProfile employee) {
        if (employee == null) {
            return null;
        }
        return employee.getFirstName() + " " + employee.getLastName();
    }
}
