package com.enterprise.empmgmt.mapper;

import com.enterprise.empmgmt.domain.entity.Department;
import com.enterprise.empmgmt.dto.response.DepartmentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    DepartmentResponse toResponse(Department department);
}
