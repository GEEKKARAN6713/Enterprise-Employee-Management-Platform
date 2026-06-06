package com.enterprise.empmgmt.mapper;

import com.enterprise.empmgmt.domain.entity.EmployeeProfile;
import com.enterprise.empmgmt.domain.entity.Task;
import com.enterprise.empmgmt.dto.response.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "assigneeName", source = "assignee", qualifiedByName = "fullName")
    @Mapping(target = "assignedById", source = "assignedBy.id")
    @Mapping(target = "assignedByName", source = "assignedBy", qualifiedByName = "fullName")
    TaskResponse toResponse(Task task);

    @Named("fullName")
    default String fullName(EmployeeProfile profile) {
        if (profile == null) {
            return null;
        }
        return profile.getFirstName() + " " + profile.getLastName();
    }
}
