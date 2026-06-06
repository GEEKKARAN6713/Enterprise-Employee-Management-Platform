package com.enterprise.empmgmt.mapper;

import com.enterprise.empmgmt.domain.entity.EmployeeProfile;
import com.enterprise.empmgmt.dto.response.EmployeeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "managerName", source = "manager", qualifiedByName = "fullName")
    EmployeeResponse toResponse(EmployeeProfile profile);

    @Named("fullName")
    default String fullName(EmployeeProfile manager) {
        if (manager == null) {
            return null;
        }
        return manager.getFirstName() + " " + manager.getLastName();
    }
}
