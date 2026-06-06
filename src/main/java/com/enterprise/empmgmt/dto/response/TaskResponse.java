package com.enterprise.empmgmt.dto.response;

import com.enterprise.empmgmt.domain.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Task response")
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate dueDate;
    private Long assigneeId;
    private String assigneeName;
    private Long assignedById;
    private String assignedByName;
    private Instant createdAt;
}
